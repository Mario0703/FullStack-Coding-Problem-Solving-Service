package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the session cannot be terminated during logout. Maps to HTTP 500.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LogoutException extends RuntimeException {

    private LogoutException(String message) {
        super(message);
    }

    public static LogoutException logoutFailed() {
        return new LogoutException("Logout failed.");
    }

    public static LogoutException logoutFailed(String message) {
        return new LogoutException(message);
    }
}
