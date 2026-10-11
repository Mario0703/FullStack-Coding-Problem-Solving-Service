package minicode.com.controller;

import minicode.com.controller.CodeEnv.EnvStrat;
import minicode.com.controller.CodeEnv.TempEnvDestroyAfter;
import minicode.com.controller.CoderExercutes.DockerCodeStrategy;
import minicode.com.auth.CurrentUser;
import minicode.com.controller.SupportedLanguages;
import minicode.com.dto.Challenge.ExecutionResult;
import minicode.com.dto.User.SubmissionRequest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.entity.User.User;
import minicode.com.exception.ForbiddenAccessException;
import minicode.com.exception.UnauthenticatedException;
import minicode.com.exception.UnSupportedLanguage;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.ContainerRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.TestRunnerRepo;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import minicode.com.service.Challange.ChallengeService;
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
    private final ChallengeService challengeService;

    public CodeController(
            ChallengeRepo challengeRepo,
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            ContainerRepo containerRepo,
            TestRunnerRepo testRunnerRepo,
            solvedChallengesRepo solvedChallengesRepository,
            ScoreService scoreService,
            ChallengeService challengeService
    ) {
        this.challengeRepo = challengeRepo;
        this.userRepo = userRepo;
        this.submissionRepo = submissionRepo;
        this.containerRepo = containerRepo;
        this.testRunnerRepo = testRunnerRepo;
        this.solvedChallengesRepository = solvedChallengesRepository;
        this.scoreService = scoreService;
        this.challengeService = challengeService;
    }

    @PostMapping("/submit")
    public ExecutionResult submitCode(
            @Valid @RequestBody SubmissionRequest codeSubmission,
            Authentication authentication
    ) throws IOException {
        UUID authenticatedUserId = CurrentUser.id(userRepo, authentication);
        validateSubmissionUser(codeSubmission, authenticatedUserId);

        User user = findUserOrThrow(authenticatedUserId);
        Challenge challenge = findChallengeOrThrow(codeSubmission.getChallengeId());
        String language = normalizeLanguage(codeSubmission.getLanguage().toLowerCase(Locale.ROOT));

        checkIfLanguageIsSupported(language);

        EnvStrat envStrategy = new TempEnvDestroyAfter(
                language,
                codeSubmission.getCode(),
                challengeService.getRunnerForLanguage(challenge, language),
                challenge.getTestCases(),
                containerRepo,
                testRunnerRepo
        );

        Path tempEnvPath = null;
        try {
            tempEnvPath = envStrategy.createEnv();
            ExecutionResult result = containerStrat.execute(language, tempEnvPath);
            SubmissionStatus status = result.status();

            Submission submission = new Submission();
            submission.setUser(user);
            submission.setChallenge(challenge);
            submission.setCode(codeSubmission.getCode());
            submission.setLanguage(language);
            submission.setExitCode(result.exitCode());
            submission.setStatus(status);
            submission.setTestCasesPassed(result.passingTestCases());
            submission.setTestCasesFailed(result.failingTestCases());
            submissionRepo.save(submission);

            if (status == SubmissionStatus.ACCEPTED) {
                SaveSolvedChallengeIfAccepted(challenge, user, result, codeSubmission.getCode(), language);
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

    private void SaveSolvedChallengeIfAccepted(Challenge challenge, User user, ExecutionResult result, String code, String language) {
        UUID userId = user.getUserId();
        UUID challengeId = challenge.getId();

        boolean alreadySolvedChallenge = solvedChallengesRepository.existsByUserIdAndChallengeId(userId, challengeId);
        boolean alreadySolvedInLanguage = solvedChallengesRepository.existsByUserIdAndChallengeIdAndLanguage(userId, challengeId, language);

        if (!alreadySolvedInLanguage) {
            SolvedChallenges solved = new SolvedChallenges();
            solved.setUserId(userId);
            solved.setChallengeId(challengeId);
            solved.setCode(code);
            solved.setLanguage(language);
            solvedChallengesRepository.save(solved);
        }

        if (alreadySolvedChallenge) {
            return;
        }

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
            throw UnSupportedLanguage.unsupportedLanguage(language);
        }
    }

    private void validateSubmissionUser(SubmissionRequest codeSubmission, UUID authenticatedUserId) {
        if (!authenticatedUserId.equals(codeSubmission.getUserId())) {
            throw ForbiddenAccessException.forbidden("User ID does not match the active session");
        }
    }

    private User findUserOrThrow(UUID userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> UnauthenticatedException.notAuthenticated(
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
        UUID userId = CurrentUser.id(userRepo, authentication);
        return solvedChallengesRepository.findChallengeIdsByUserId(userId);
    }
}
