package minicode.com.dto.User;

import java.util.UUID;

public record UserSummary(
        UUID userId,
        String userName,
        String email,
        int score,
        int numberOfEasyChallengesSolved,
        int numberOfMediumChallengesSolved,
        int numberOfHardChallengesSolved
) {
}
