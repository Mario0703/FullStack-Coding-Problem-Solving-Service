package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the code executor fails for an infrastructure reason (e.g. the
 * docker process could not be started). Expected submission outcomes (compile
 * error, wrong answer, timeout) are returned as data, not thrown. Maps to 500.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CodeExercutorException extends RuntimeException {

    private CodeExercutorException(String message) {
        super(message);
    }

    public static CodeExercutorException failedToRunCodeException() {
        return new CodeExercutorException("Failed to run code.");
    }

    public static CodeExercutorException failedToCompileCodeException() {
        return new CodeExercutorException("Failed to compile code.");
    }

    public static CodeExercutorException failedToRunTestCasesException() {
        return new CodeExercutorException("Failed to run test cases.");
    }
}
