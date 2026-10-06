package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the caller is authenticated but lacks the required role.
 *
 * <p>{@code @ResponseStatus} maps it to HTTP 403 wherever it is thrown from a
 * controller/service. Named "ForbiddenAccess" rather than "AccessDenied" to
 * avoid colliding with Spring Security's
 * {@code org.springframework.security.access.AccessDeniedException}, which is
 * handled by the security filter chain rather than MVC.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenAccessException extends RuntimeException {

    private ForbiddenAccessException(String message) {
        super(message);
    }

    public static ForbiddenAccessException adminRequired() {
        return new ForbiddenAccessException("Access denied: admin privileges required.");
    }

    public static ForbiddenAccessException userRequired() {
        return new ForbiddenAccessException("Access denied: user privileges required.");
    }

    public static ForbiddenAccessException adminOrUserRequired() {
        return new ForbiddenAccessException("Access denied: admin or user privileges required.");
    }

    public static ForbiddenAccessException forbidden(String message) {
        return new ForbiddenAccessException(message);
    }
}
