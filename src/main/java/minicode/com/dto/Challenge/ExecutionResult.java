package minicode.com.dto.Challenge;

import minicode.com.entity.Submissions.SubmissionStatus;

public record ExecutionResult(
        int exitCode,
        String output,
        int passingTestCases,
        int failingTestCases,
        SubmissionStatus status
) {
}




