package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Thrown when a request names a language the platform cannot execute. Maps to HTTP 400. */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnSupportedLanguage extends RuntimeException {

    private UnSupportedLanguage(String message) {
        super(message);
    }

    public static UnSupportedLanguage unsupportedLanguage(String language) {
        return new UnSupportedLanguage("Unsupported language: " + language);
    }
}
