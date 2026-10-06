package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Thrown when no valid session is present; maps to HTTP 401. */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthenticatedException extends RuntimeException {

    private UnauthenticatedException(String message) {
        super(message);
    }

    public static UnauthenticatedException authenticationRequired() {
        return new UnauthenticatedException("Authentication required.");
    }

    public static UnauthenticatedException notAuthenticated(String message) {
        return new UnauthenticatedException(message);
    }
}
