package minicode.com.controller.Challenges;

import minicode.com.auth.Access;
import minicode.com.dto.User.SolvedChallengeResponse;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Submissions.Submission;
import minicode.com.exception.UnauthenticatedException;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;


public final class SubmissionHelper {

    private SubmissionHelper() {
    }

    public static UUID authenticatedUserId(UserRepo userRepo, Authentication authentication) {
        Access.requireUser(authentication);
        return userRepo.findFirstByUserName(authentication.getName())
                .orElseThrow(UnauthenticatedException::authenticationRequired)
                .getUserId();
    }

    public static List<Integer> passedTestCasesForUser(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication
    ) {
        return testCaseValues(
                submissionRepo.findReceiptByUserId(authenticatedUserId(userRepo, authentication)),
                Submission::getTestCasesPassed
        );
    }

    public static List<Integer> failedTestCasesForUser(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication
    ) {
        return testCaseValues(
                submissionRepo.findReceiptByUserId(authenticatedUserId(userRepo, authentication)),
                Submission::getTestCasesFailed
        );
    }

    public static List<Integer> totalTestCasesForUser(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication
    ) {
        return testCaseValues(
                submissionRepo.findReceiptByUserId(authenticatedUserId(userRepo, authentication)),
                submission -> submission.getChallenge().getNumberOfTestCases()
        );
    }

    public static List<List<Integer>> submissionStats(SubmissionRepo submissionRepo, UUID userId) {
        List<Submission> submissions = submissionRepo.findPublicSubmissionsByUserId(userId);
        return List.of(
                testCaseValues(submissions, Submission::getTestCasesPassed),
                testCaseValues(submissions, Submission::getTestCasesFailed),
                testCaseValues(submissions, submission -> submission.getChallenge().getNumberOfTestCases())
        );
    }

    private static List<Integer> testCaseValues(
            List<Submission> submissions,
            ToIntFunction<Submission> extractor
    ) {
        return submissions.stream()
                .mapToInt(extractor)
                .boxed()
                .toList();
    }


    public static List<SolvedChallengeResponse> buildSolvedChallengeResponses(
            ChallengeRepo challengeRepo,
            List<SolvedChallenges> solvedChallenges
    ) {
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
}
