package minicode.com.dto;

import minicode.com.entity.Submission;

import java.time.Instant;
import java.util.UUID;

public record SubmissionResponse(
        UUID id,
        UUID challengeId,
        String challengeName,
        String difficulty,
        String code,
        String language,
        int exitCode,
        String status,
        int testCasesPassed,
        int testCasesFailed,
        int totalTestCases,
        Instant createdAt
) {
    public static SubmissionResponse from(Submission submission) {
        return new SubmissionResponse(
                submission.getId(),
                submission.getChallenge().getId(),
                submission.getChallenge().getName(),
                submission.getChallenge().getDifficulty().name(),
                submission.getCode(),
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
