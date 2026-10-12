package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginRequestTest {

    private LoginRequest loginRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.loginRequest = new LoginRequest();
    }

    @Test
    public void allUsersMustHaveAUserName(){
        loginRequest.setUserName(null);
        Boolean hasViolation = hasViolationOn(validator.validate(loginRequest), "userName");
        assertTrue(hasViolation, "Expected violation on userName when it is null");
    }

    @Test
    public void allUsersMustHaveAnEmail(){
        loginRequest.setEmail(null);
        Boolean hasViolation = hasViolationOn(validator.validate(loginRequest), "email");
        assertTrue(hasViolation, "Expected violation on email when it is null");
    }

    @Test
    public void allUsersMustHaveAPassword(){
        loginRequest.setPassword(null);
        Boolean hasViolation = hasViolationOn(validator.validate(loginRequest), "password");
        assertTrue(hasViolation, "Expected violation on password when it is null");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<LoginRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }


}
