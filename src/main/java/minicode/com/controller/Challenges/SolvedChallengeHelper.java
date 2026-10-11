package minicode.com.controller.Challenges;

import minicode.com.dto.User.SolvedChallengeResponse;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.repository.ChallengeRepo;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SolvedChallengeHelper {

    private SolvedChallengeHelper() {
    }

    /**
     * Collapses the one-row-per-(challenge, language) solved rows into one
     * response per challenge, keeping the most recent solve date and every
     * language the user solved it in. Rows whose challenge no longer exists
     * are skipped.
     */
    public static List<SolvedChallengeResponse> buildSolvedChallengeResponses(
            ChallengeRepo challengeRepo,
            List<SolvedChallenges> solvedChallenges
    ) {
        Map<UUID, List<SolvedChallenges>> rowsByChallenge = groupByChallenge(solvedChallenges);
        if (rowsByChallenge.isEmpty()) {
            return List.of();
        }

        Map<UUID, Challenge> challengesById = findChallenges(challengeRepo, rowsByChallenge.keySet());
        return toResponses(challengesById, rowsByChallenge);
    }

    private static Map<UUID, List<SolvedChallenges>> groupByChallenge(List<SolvedChallenges> solvedChallenges) {
        return solvedChallenges.stream()
                .collect(Collectors.groupingBy(
                        SolvedChallenges::getChallengeId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    private static Map<UUID, Challenge> findChallenges(ChallengeRepo challengeRepo, Set<UUID> challengeIds) {
        return challengeRepo.findAllById(challengeIds).stream()
                .collect(Collectors.toMap(Challenge::getId, Function.identity()));
    }

    private static List<SolvedChallengeResponse> toResponses(
            Map<UUID, Challenge> challengesById,
            Map<UUID, List<SolvedChallenges>> rowsByChallenge
    ) {
        return rowsByChallenge.entrySet().stream()
                .filter(entry -> challengesById.containsKey(entry.getKey()))
                .map(entry -> toResponse(entry.getKey(), challengesById.get(entry.getKey()), entry.getValue()))
                .toList();
    }

    private static SolvedChallengeResponse toResponse(
            UUID challengeId,
            Challenge challenge,
            List<SolvedChallenges> rows
    ) {
        Instant latestSolve = rows.stream()
                .map(SolvedChallenges::getSolvedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);
        List<String> languages = rows.stream()
                .map(SolvedChallenges::getLanguage)
                .toList();
        return new SolvedChallengeResponse(
                challengeId,
                challenge.getName(),
                challenge.getDifficulty().name(),
                latestSolve,
                languages
        );
    }
}
