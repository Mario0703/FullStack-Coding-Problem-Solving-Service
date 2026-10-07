package minicode.com.dto.Challenge;

import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.InputOutput;
import minicode.com.entity.Challange.TestCases;
import minicode.com.entity.Submissions.Submission;

import java.util.List;
import java.util.UUID;

/**
 * Everything the MiniCode AI helper may read for one challenge, gathered in a
 * single request so the assistant does not have to hop between the older,
 * admin-guarded endpoints.
 *
 * <p>The instructor {@code solution} is only filled in once the caller has an
 * accepted submission (or is an admin), mirroring {@code /Solution/{id}}.
 * {@code userCode} is the caller's own most recent submission for the
 * challenge, if any.
 */
public record ChallengeAiContextResponse(
        UUID id,
        String name,
        String description,
        String difficulty,
        String category,
        String language,
        List<Example> examples,
        List<TestCase> testCases,
        List<String> hints,
        String solution,
        String userCode,
        String userLanguage,
        boolean solved
) {

    public record Example(String input, String output) {
        static Example from(InputOutput value) {
            return new Example(value.getInputExample(), value.getOutputExample());
        }
    }

    public record TestCase(String input, String expectedOutput) {
        static TestCase from(TestCases value) {
            return new TestCase(value.getTestInput(), value.getTestOutput());
        }
    }

    public static ChallengeAiContextResponse from(
            Challenge challenge,
            List<String> hints,
            String solution,
            Submission latestSubmission,
            boolean solved
    ) {
        return new ChallengeAiContextResponse(
                challenge.getId(),
                challenge.getName(),
                challenge.getDescription(),
                challenge.getDifficulty().name(),
                challenge.getCategory().name(),
                challenge.getLanguage(),
                challenge.getInputOutput().stream().map(Example::from).toList(),
                challenge.getTestCases().stream().map(TestCase::from).toList(),
                hints,
                solution,
                latestSubmission != null ? latestSubmission.getCode() : null,
                latestSubmission != null ? latestSubmission.getLanguage() : null,
                solved
        );
    }
}
