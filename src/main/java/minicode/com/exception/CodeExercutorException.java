package minicode.com.exception;

public class CodeExercutorException extends RuntimeException {
    public CodeExercutorException(String message) {
        super(message);
    }
    public static failedToRunCodeException() {
        return new CodeExercutorException("Failed to run code.");
    }

    public static failedToCompileCodeException() {
        return new CodeExercutorException("Failed to compile code.");
    }

    public static failedToRunTestCasesException() {
        return new CodeExercutorException("Failed to run test cases.");
    }
}
