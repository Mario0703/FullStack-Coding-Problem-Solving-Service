package minicode.com.dto;

public record ExecutionResult(
        int exitCode,
        String output,
        int passingTestCases,
        int failingTestCases
) {
}
