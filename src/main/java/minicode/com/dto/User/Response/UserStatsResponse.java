package minicode.com.dto.User.Response;

import java.util.UUID;

public record UserStatsResponse(
        UUID userId,
        String userName,
        int score,
        int numberOfEasyChallengesSolved,
        int numberOfMediumChallengesSolved,
        int numberOfHardChallengesSolved,
        int totalChallengesSolved) {
}