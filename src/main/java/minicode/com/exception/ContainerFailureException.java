package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the execution environment itself could not be prepared: missing
 * seeded container/test-runner metadata, or a failure creating the temporary
 * environment. Maps to HTTP 500.
 *
 * <p>Not used for a submission's build/compile/run outcome — those are expected
 * results reported as data through the {@code ExecutionResult} status.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ContainerFailureException extends RuntimeException {

    private ContainerFailureException(String message) {
        super(message);
    }

    public static ContainerFailureException containerFailed(String message) {
        return new ContainerFailureException(message);
    }

    public static ContainerFailureException failedToCreateContainer(String message) {
        return new ContainerFailureException(message);
    }

    public static ContainerFailureException failedToStartContainer(String message) {
        return new ContainerFailureException(message);
    }

    public static ContainerFailureException failedToRunContainer(String message) {
        return new ContainerFailureException(message);
    }
}
