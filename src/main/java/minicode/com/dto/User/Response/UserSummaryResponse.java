package minicode.com.dto.User.Response;

import java.util.UUID;

public record UserSummaryResponse(
        UUID userId,
        String userName,
        String email,
        int score,
        int numberOfEasyChallengesSolved,
        int numberOfMediumChallengesSolved,
        int numberOfHardChallengesSolved,
        int totalChallengesSolved
) {
}
