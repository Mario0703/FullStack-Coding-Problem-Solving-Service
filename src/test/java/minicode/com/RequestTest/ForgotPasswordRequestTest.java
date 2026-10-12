package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.Request.ForgotPasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ForgotPasswordRequestTest {

    private ForgotPasswordRequest forgotPasswordRequest;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.forgotPasswordRequest = new ForgotPasswordRequest();
    }

    @Test
    public void validForgotPasswordRequestHasNoViolations() {
        forgotPasswordRequest.setEmail("user@example.com");
        Set<ConstraintViolation<ForgotPasswordRequest>> violations = validator.validate(forgotPasswordRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void emailCannotBeNull() {
        forgotPasswordRequest.setEmail(null);
        Boolean hasViolation = hasViolationOn(validator.validate(forgotPasswordRequest), "email");
        assertTrue(hasViolation, "Expected violation on email when it is null");
    }

    @Test
    public void emailCannotBeBlank() {
        forgotPasswordRequest.setEmail("");
        Boolean hasViolation = hasViolationOn(validator.validate(forgotPasswordRequest), "email");
        assertTrue(hasViolation, "Expected violation on email when it is blank");
    }

    @Test
    public void emailMustBeAValidAddress() {
        forgotPasswordRequest.setEmail("not-an-email");
        Boolean hasViolation = hasViolationOn(validator.validate(forgotPasswordRequest), "email");
        assertTrue(hasViolation, "Expected violation on email when it is not a valid address");
    }

    @Test
    public void emailCannotExceedMaxLength() {
        forgotPasswordRequest.setEmail("a".repeat(100) + "@example.com");
        Boolean hasViolation = hasViolationOn(validator.validate(forgotPasswordRequest), "email");
        assertTrue(hasViolation, "Expected violation on email when it exceeds 100 characters");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<ForgotPasswordRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}