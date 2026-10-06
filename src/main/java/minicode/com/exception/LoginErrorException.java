package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a login attempt fails (unknown account or wrong password).
 * Maps to HTTP 401.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginErrorException extends RuntimeException {

    private LoginErrorException(String message) {
        super(message);
    }

    public static LoginErrorException invalidCredentials(String message) {
        return new LoginErrorException(message);
    }
}
