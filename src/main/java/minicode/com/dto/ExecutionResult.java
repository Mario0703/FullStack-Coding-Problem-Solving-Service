package minicode.com.dto;

public record ExecutionResult(
        int exitCode,
        String output
) {
}
