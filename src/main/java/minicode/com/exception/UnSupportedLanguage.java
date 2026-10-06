package minicode.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnSupportedLanguage extends RuntimeException {
    public UnSupportedLanguage(String message) {
        super(message);
    }

    public static UnSupportedLanguage unsupportedLanguage(String language) {
        return new UnSupportedLanguage("Unsupported language: " + language);
    }

}
