package minicode.com.service.Challange;

import java.util.UUID;

/**
 * Everything needed to record one accepted submission: a solved row (once
 * per language) and, for the first solve of a challenge, its score award.
 */
public record AcceptedSubmission(
        UUID userId,
        UUID challengeId,
        String difficulty,
        String language,
        String code,
        int passingTestCases,
        int failingTestCases) {

    public int totalTestCases() {
        return passingTestCases + failingTestCases;
    }
}