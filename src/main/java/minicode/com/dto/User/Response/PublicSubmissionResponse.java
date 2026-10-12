package minicode.com.dto.User.Response;

import minicode.com.entity.Submissions.Submission;

import java.time.Instant;
import java.util.UUID;

public record PublicSubmissionResponse(
        UUID id,
        UUID challengeId,
        String challengeName,
        String difficulty,
        String language,
        int exitCode,
        String status,
        int testCasesPassed,
        int testCasesFailed,
        int totalTestCases,
        Instant createdAt
) {
    public static PublicSubmissionResponse from(Submission submission) {
        return new PublicSubmissionResponse(
                submission.getId(),
                submission.getChallenge().getId(),
                submission.getChallenge().getName(),
                submission.getChallenge().getDifficulty().name(),
                submission.getLanguage(),
                submission.getExitCode(),
                submission.getStatus().name(),
                submission.getTestCasesPassed(),
                submission.getTestCasesFailed(),
                submission.getChallenge().getNumberOfTestCases(),
                submission.getCreatedAt()
        );
    }
}
