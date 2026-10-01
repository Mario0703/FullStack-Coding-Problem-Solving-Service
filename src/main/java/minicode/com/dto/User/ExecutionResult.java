package minicode.com.dto.User;

public record ExecutionResult(
        int exitCode,
        String output,
        int passingTestCases,
        int failingTestCases
) {
}




