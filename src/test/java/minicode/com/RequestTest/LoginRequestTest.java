package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.Request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
    public void loginWithUserNameAndPasswordIsValid(){
        loginRequest.setUserName("tester");
        loginRequest.setPassword("Password123");

        assertTrue(validator.validate(loginRequest).isEmpty());
    }

    @Test
    public void loginWithEmailAndPasswordIsValid(){
        loginRequest.setEmail("tester@example.com");
        loginRequest.setPassword("Password123");

        assertTrue(validator.validate(loginRequest).isEmpty());
    }

    @Test
    public void loginWithoutAnIdentifierIsRejected(){
        loginRequest.setPassword("Password123");

        assertFalse(validator.validate(loginRequest).isEmpty());
    }

    @Test
    public void loginWithBothUserNameAndEmailIsRejected(){
        loginRequest.setUserName("tester");
        loginRequest.setEmail("tester@example.com");
        loginRequest.setPassword("Password123");

        assertFalse(validator.validate(loginRequest).isEmpty());
    }

    @Test
    public void loginWithoutPasswordIsRejected(){
        loginRequest.setUserName("tester");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertTrue(hasViolationOn(violations, "password"));
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<LoginRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }


}
