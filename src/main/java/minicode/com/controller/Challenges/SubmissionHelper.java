package minicode.com.controller.Challenges;

import minicode.com.auth.Access;
import minicode.com.dto.User.SolvedChallengeResponse;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.exception.UnauthenticatedException;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stateless helpers for {@link submissionController}. The controller owns the
 * repositories, so they are passed in and every method is static.
 */
public final class SubmissionHelper {

    private SubmissionHelper() {
    }

    public static UUID authenticatedUserId(UserRepo userRepo, Authentication authentication) {
        Access.requireUser(authentication);
        return userRepo.findFirstByUserName(authentication.getName())
                .orElseThrow(UnauthenticatedException::authenticationRequired)
                .getUserId();
    }

    public static List<Integer> testCaseValuesForUser(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication,
            boolean passed
    ) {
        UUID userId = authenticatedUserId(userRepo, authentication);
        if (passed) {
            return submissionRepo.findReceiptPassedTestCasesByUserId(userId);
        }
        return submissionRepo.findReceiptFailedTestCasesByUserId(userId);
    }

    /**
     * Collapses the per-language SolvedChallenges rows into exactly one
     * response per challenge, keeping the languages the user solved it in
     * and the most recent solve date.
     */
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
