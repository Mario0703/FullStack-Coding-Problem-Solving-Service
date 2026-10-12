package minicode.com.dto.Challenge.Response;

import minicode.com.entity.Submissions.SubmissionStatus;

public record ExecutionResultResponse(
        int exitCode,
        String output,
        int passingTestCases,
        int failingTestCases,
        SubmissionStatus status
) {
}




