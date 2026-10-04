package minicode.com.dto.User;

public record UserStats(
        String userName,
        int score,
        int numberOfEasyChallengesSolved,
        int numberOfMediumChallengesSolved,
        int numberOfHardChallengesSolved,
        int totalChallengesSolved) {
}
