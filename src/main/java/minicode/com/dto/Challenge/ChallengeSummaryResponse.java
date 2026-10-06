package minicode.com.dto.Challenge;

import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;

import java.util.UUID;

/**
 * Public, non-sensitive view of a challenge.
 *
 * <p>Deliberately omits the hidden test cases, the runner, the instructor
 * solution and the submission history: none of these may ever reach a solver
 * (nor an unauthenticated caller). Use this DTO for any endpoint that lists
 * challenges. Admins fetch the sensitive parts through their own,
 * admin-guarded endpoints (test cases, solution).
 */
public record ChallengeSummaryResponse(
        UUID id,
        String name,
        String description,
        Difficulty difficulty,
        Category category,
        int numberOfTestCases,
        String language
) {

    public static ChallengeSummaryResponse from(Challenge challenge) {
        return new ChallengeSummaryResponse(
                challenge.getId(),
                challenge.getName(),
                challenge.getDescription(),
                challenge.getDifficulty(),
                challenge.getCategory(),
                challenge.getNumberOfTestCases(),
                challenge.getLanguage()
        );
    }
}
