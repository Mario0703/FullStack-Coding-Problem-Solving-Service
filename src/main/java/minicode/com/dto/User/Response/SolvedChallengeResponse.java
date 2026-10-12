package minicode.com.dto.User.Response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * A challenge the user has solved, extracted from the SolvedChallenges table.
 * There is exactly one entry per solved challenge, no matter how often the
 * user re-solved it or in how many languages.
 */
public record SolvedChallengeResponse(
        UUID challengeId,
        String challengeName,
        String difficulty,
        Instant solvedAt,
        List<String> languages
) {
}