package minicode.com.dto.Challenge;

public record ExecutionResult(
        int exitCode,
        String output,
        int passingTestCases,
        int failingTestCases
) {
}




