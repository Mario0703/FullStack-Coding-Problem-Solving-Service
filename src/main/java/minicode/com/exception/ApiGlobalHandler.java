package minicode.com.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The single place that turns an exception into an HTTP response.
 *
 * <p>The returned body goes to the frontend; the log line goes to the terminal.
 * 4xx are logged at WARNING, 5xx at SEVERE (with the stack trace). The bodies
 * never contain stack traces or database details.
 */
@RestControllerAdvice
public class ApiGlobalHandler {

    private static final Logger logger = Logger.getLogger(ApiGlobalHandler.class.getName());

    // --- Custom exceptions -------------------------------------------------

    @ExceptionHandler(LoginErrorException.class)
    public ResponseEntity<String> handleLogin(LoginErrorException error) {
        return respond(HttpStatus.UNAUTHORIZED, error, "Login error");
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> handleUnauthenticated(UnauthenticatedException error) {
        return respond(HttpStatus.UNAUTHORIZED, error, "Authentication error");
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<String> handleForbidden(ForbiddenAccessException error) {
        return respond(HttpStatus.FORBIDDEN, error, "Authorization error");
    }

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<String> handleSignup(SignupException error) {
        return respond(HttpStatus.BAD_REQUEST, error, "Signup error");
    }

    @ExceptionHandler(UnSupportedLanguage.class)
    public ResponseEntity<String> handleUnsupportedLanguage(UnSupportedLanguage error) {
        return respond(HttpStatus.BAD_REQUEST, error, "Unsupported language");
    }

    @ExceptionHandler(ContainerFailureException.class)
    public ResponseEntity<String> handleContainerFailure(ContainerFailureException error) {
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, error, "Container failure");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException error) {
        String message = error.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.warning("Invalid input: " + message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDatabase(DataIntegrityViolationException error) {
        logger.log(Level.SEVERE, "Database error", error);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("The request conflicts with existing data");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatus(ResponseStatusException error) {
        HttpStatus status = HttpStatus.resolve(error.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String message = error.getReason() != null ? error.getReason() : status.getReasonPhrase();
        return respond(status, message, error, "Request error");
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException error) {
        return respond(HttpStatus.FORBIDDEN, "Access denied", error, "Authorization error");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthentication(AuthenticationException error) {
        return respond(HttpStatus.UNAUTHORIZED, "Invalid credentials", error, "Authentication error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception error) {
        logger.log(Level.SEVERE, "Unexpected error", error);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }


    private ResponseEntity<String> respond(HttpStatus status, Exception error, String context) {
        return respond(status, error.getMessage(), error, context);
    }

    private ResponseEntity<String> respond(
            HttpStatus status,
            String message,
            Throwable cause,
            String context
    ) {
        if (status.is5xxServerError()) {
            logger.log(Level.SEVERE, context + ": " + message, cause);
        } else {
            logger.warning(context + ": " + message);
        }
        return ResponseEntity.status(status).body(message);
    }
}
