package minicode.com.controller;

import minicode.com.auth.Access;
import minicode.com.dto.User.SolvedChallengeResponse;
import minicode.com.dto.User.SubmissionResponse;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Submissions.Submission;
import minicode.com.exception.UnauthenticatedException;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/submission")
public class submissionController {
    private final SubmissionRepo SubmissionRepo;
    private final UserRepo userRepo;
    private final solvedChallengesRepo solvedChallengesRepo;
    private final ChallengeRepo challengeRepo;

    public submissionController(
            SubmissionRepo submissionRepo,
            UserRepo userRepo,
            solvedChallengesRepo solvedChallengesRepo,
            ChallengeRepo challengeRepo
    ) {
        this.SubmissionRepo = submissionRepo;
        this.userRepo = userRepo;
        this.solvedChallengesRepo = solvedChallengesRepo;
        this.challengeRepo = challengeRepo;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<SubmissionResponse> listUserSubmissions(Authentication authentication) {
        UUID userId = authenticatedUserId(authentication);
        return SubmissionRepo.findReceiptByUserId(userId).stream()
                .map(SubmissionResponse::from)
                .toList();
    }

    @GetMapping("/solved")
    @Transactional(readOnly = true)
    public List<SolvedChallengeResponse> listSolvedChallenges(Authentication authentication) {
        UUID userId = authenticatedUserId(authentication);
        return buildSolvedChallengeResponses(solvedChallengesRepo.findAllByUserId(userId));
    }

    /**
     * Collapses the per-language SolvedChallenges rows into exactly one
     * response per challenge, keeping the languages the user solved it in
     * and the most recent solve date.
     */
    private List<SolvedChallengeResponse> buildSolvedChallengeResponses(List<SolvedChallenges> solvedChallenges) {
        Map<UUID, List<SolvedChallenges>> byChallenge = solvedChallenges.stream()
                .collect(Collectors.groupingBy(
                        SolvedChallenges::getChallengeId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        if (byChallenge.isEmpty()) {
            return List.of();
        }

        Map<UUID, Challenge> challenges = challengeRepo.findAllById(byChallenge.keySet()).stream()
                .collect(Collectors.toMap(Challenge::getId, Function.identity()));

        return byChallenge.entrySet().stream()
                .filter(entry -> challenges.containsKey(entry.getKey()))
                .map(entry -> {
                    Challenge challenge = challenges.get(entry.getKey());
                    List<SolvedChallenges> rows = entry.getValue();
                    Instant latestSolve = rows.stream()
                            .map(SolvedChallenges::getSolvedAt)
                            .max(Comparator.naturalOrder())
                            .orElse(null);
                    List<String> languages = rows.stream()
                            .map(SolvedChallenges::getLanguage)
                            .toList();
                    return new SolvedChallengeResponse(
                            entry.getKey(),
                            challenge.getName(),
                            challenge.getDifficulty().name(),
                            latestSolve,
                            languages
                    );
                })
                .toList();
    }

    @GetMapping("/details")
    @Transactional(readOnly = true)
    public SubmissionResponse getDetailsFromSubmissionId(
            Authentication authentication,
            @RequestParam UUID submissionId
    ) {
        UUID userId = authenticatedUserId(authentication);

        Submission submission = SubmissionRepo.findByIdAndUser_UserId(submissionId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Submission not found"
                ));

        return SubmissionResponse.from(submission);
    }

    @GetMapping("/passed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfPassedTestCases(Authentication authentication) {
        return testCaseValuesForUser(authentication, true);
    }

    @GetMapping("/failed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfFailedTestCases(Authentication authentication) {
        return testCaseValuesForUser(authentication, false);
    }

    @GetMapping("/total")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfTotalTestCases(Authentication authentication) {
        UUID userId = authenticatedUserId(authentication);
        return SubmissionRepo.findReceiptTotalTestCasesByUserId(userId);
    }

    private List<Integer> testCaseValuesForUser(
            Authentication authentication,
            boolean passed
    ) {
        UUID userId = authenticatedUserId(authentication);
        return passed
                ? SubmissionRepo.findReceiptPassedTestCasesByUserId(userId)
                : SubmissionRepo.findReceiptFailedTestCasesByUserId(userId);
    }

    private UUID authenticatedUserId(Authentication authentication) {
        Access.requireUser(authentication);
        return userRepo.findFirstByUserName(authentication.getName())
                .orElseThrow(UnauthenticatedException::authenticationRequired)
                .getUserId();
    }
}