package minicode.com.dto.User.Response;

public record UserStatsResponse(
        String userName,
        int score,
        int numberOfEasyChallengesSolved,
        int numberOfMediumChallengesSolved,
        int numberOfHardChallengesSolved,
        int totalChallengesSolved) {
}
