package minicode.com.controller;

import minicode.com.controller.CodeEnv.EnvStrat;
import minicode.com.controller.CodeEnv.TempEnvDestroyAfter;
import minicode.com.controller.CoderExercutes.DockerCodeStrategy;
import minicode.com.controller.SupportedLanguages;
import minicode.com.dto.Challenge.ExecutionResult;
import minicode.com.dto.User.SubmissionRequest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.ContainerRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.TestRunnerRepo;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import minicode.com.service.scoreService.ScoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/api/code/playground")
public class CodeController {
    private final DockerCodeStrategy containerStrat = new DockerCodeStrategy();
    private final ChallengeRepo challengeRepo;
    private final UserRepo userRepo;
    private final SubmissionRepo submissionRepo;
    private final ContainerRepo containerRepo;
    private final TestRunnerRepo testRunnerRepo;
    private final solvedChallengesRepo solvedChallengesRepository;
    private final ScoreService scoreService;

    public CodeController(
            ChallengeRepo challengeRepo,
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            ContainerRepo containerRepo,
            TestRunnerRepo testRunnerRepo,
            solvedChallengesRepo solvedChallengesRepository,
            ScoreService scoreService
    ) {
        this.challengeRepo = challengeRepo;
        this.userRepo = userRepo;
        this.submissionRepo = submissionRepo;
        this.containerRepo = containerRepo;
        this.testRunnerRepo = testRunnerRepo;
        this.solvedChallengesRepository = solvedChallengesRepository;
        this.scoreService = scoreService;
    }

    @PostMapping("/submit")
    public ExecutionResult submitCode(
            @Valid @RequestBody SubmissionRequest codeSubmission,
            Authentication authentication
    ) throws IOException {
        UUID authenticatedUserId = getAuthenticatedUserId(authentication);
        validateSubmissionUser(codeSubmission, authenticatedUserId);

        User user = findUserOrThrow(authenticatedUserId);
        Challenge challenge = findChallengeOrThrow(codeSubmission.getChallengeId());
        String language = normalizeLanguage(codeSubmission.getLanguage().toLowerCase(Locale.ROOT));

        checkIfLanguageIsSupported(language);

        EnvStrat envStrategy = new TempEnvDestroyAfter(
                language,
                codeSubmission.getCode(),
                challenge.getRunner(),
                challenge.getTestCases(),
                containerRepo,
                testRunnerRepo
        );

        Path tempEnvPath = null;
        try {
            tempEnvPath = envStrategy.createEnv();
            ExecutionResult result = containerStrat.execute(tempEnvPath);
            SubmissionStatus status;

            if (result.exitCode() == 0){
                status = SubmissionStatus.ACCEPTED;

            }
            else{
                status = SubmissionStatus.WRONG_ANSWER;
            }

            submissionRepo.save(new Submission(
                    user,
                    challenge,
                    codeSubmission.getCode(),
                    language,
                    result.exitCode(),
                    status,
                    result.passingTestCases(),
                    result.failingTestCases()
            ));

            if (status == SubmissionStatus.ACCEPTED) {
                SaveSolvedChallengeIfAccepted(challenge, user, result, codeSubmission.getCode());
            }

            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Code execution was interrupted", e);
        } finally {
            if (tempEnvPath != null) {
                try {
                    envStrategy.destroyEnv(tempEnvPath);
                } catch (IOException e) {
                    // Do not hide the submission result if cleanup fails.
                    e.printStackTrace();
                }
            }
        }
    }

    private void SaveSolvedChallengeIfAccepted(Challenge challenge, User user, ExecutionResult result, String code) {
        UUID userId = user.getUserId();
        UUID challengeId = challenge.getId();

        if (solvedChallengesRepository.existsByUserIdAndChallengeId(userId, challengeId)) {
            return;
        }

        solvedChallengesRepository.save(new SolvedChallenges(challenge, user, code));

        int totalTestCases = result.passingTestCases() + result.failingTestCases();
        scoreService.updateScoreAndSolvedCounters(
                userId,
                totalTestCases,
                result.passingTestCases(),
                challenge.getDifficulty().name()
        );
    }
    //remove later
    private String normalizeLanguage(String language) {
        return switch (language) {
            case "c#" -> "csharp";
            case "c++" -> "cplusplus";
            default -> language;
        };
    }

    private void checkIfLanguageIsSupported(String language) {
        try {
            SupportedLanguages.valueOf(language.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported programming language");
        }
    }

    private void validateSubmissionUser(SubmissionRequest codeSubmission, UUID authenticatedUserId) {
        if (!authenticatedUserId.equals(codeSubmission.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User ID does not match the active session");
        }
    }

    private User findUserOrThrow(UUID userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user was not found"
                ));
    }

    private Challenge findChallengeOrThrow(UUID challengeId) {
        return challengeRepo.findById(challengeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Challenge not found"
                ));
    }

    @GetMapping("/SolvedChallenges")
    public List<UUID> getSolvedChallenges(Authentication authentication) {
        UUID userId = getAuthenticatedUserId(authentication);
        return submissionRepo.findChallengeIdsByUserIdAndStatus(
                userId,
                SubmissionStatus.ACCEPTED
        );
    }

    private UUID getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        User user = userRepo.findFirstByUserName(authentication.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated"));
        if (user.getRole() == UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User access is not permitted for an admin session");
        }

        return user.getUserId();
    }
}
