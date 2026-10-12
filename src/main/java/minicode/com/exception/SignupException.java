package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a signup request is rejected (invalid input, or a username/email
 * that is already taken). Maps to HTTP 400.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SignupException extends RuntimeException {

    private SignupException(String message) {
        super(message);
    }

    public static SignupException usernameTaken(String userName) {
        return new SignupException("Username already exists: " + userName);
    }

    public static SignupException emailTaken(String email) {
        return new SignupException("Email already exists: " + email);
    }
}
