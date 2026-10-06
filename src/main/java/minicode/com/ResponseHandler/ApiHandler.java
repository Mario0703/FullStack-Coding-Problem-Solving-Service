package minicode.com.ResponseHandler;

import minicode.com.exception.LoginErrorException;
import minicode.com.exception.SignupException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.logging.Logger;

@RestControllerAdvice
public class ApiHandler {

    private Logger logger = Logger.getLogger(ApiHandler.class.getName());

    @ExceptionHandler(LoginErrorException.class)
    public ResponseEntity<String> handleLogin(LoginErrorException Error) {
        Error error = new Error(Error.getMessage());
        logger.warning("Login error: " + error.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error.getMessage());
    }

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<String> handleSignup(SignupException Error) {
        Error error = new Error(Error.getMessage());
        logger.warning("Signup error: " + error.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
    }



}
