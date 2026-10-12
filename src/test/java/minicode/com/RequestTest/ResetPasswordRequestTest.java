package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.Request.ResetPasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResetPasswordRequestTest {

    private ResetPasswordRequest resetPasswordRequest;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.resetPasswordRequest = new ResetPasswordRequest();
    }

    @Test
    public void validResetPasswordRequestHasNoViolations() {
        resetPasswordRequest.setToken("token-123");
        resetPasswordRequest.setNewPassword("NewPassword123");
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(resetPasswordRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void tokenCannotBeNull() {
        resetPasswordRequest.setToken(null);
        resetPasswordRequest.setNewPassword("NewPassword123");
        Boolean hasViolation = hasViolationOn(validator.validate(resetPasswordRequest), "token");
        assertTrue(hasViolation, "Expected violation on token when it is null");
    }

    @Test
    public void tokenCannotBeBlank() {
        resetPasswordRequest.setToken("");
        resetPasswordRequest.setNewPassword("NewPassword123");
        Boolean hasViolation = hasViolationOn(validator.validate(resetPasswordRequest), "token");
        assertTrue(hasViolation, "Expected violation on token when it is blank");
    }

    @Test
    public void newPasswordCannotBeBlank() {
        resetPasswordRequest.setToken("token-123");
        resetPasswordRequest.setNewPassword("");
        Boolean hasViolation = hasViolationOn(validator.validate(resetPasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it is blank");
    }

    @Test
    public void newPasswordCannotBeShorterThanMinLength() {
        resetPasswordRequest.setToken("token-123");
        resetPasswordRequest.setNewPassword("Ab1");
        Boolean hasViolation = hasViolationOn(validator.validate(resetPasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it is shorter than 8 characters");
    }

    @Test
    public void newPasswordMustContainAnUppercaseLetter() {
        resetPasswordRequest.setToken("token-123");
        resetPasswordRequest.setNewPassword("lowercase123");
        Boolean hasViolation = hasViolationOn(validator.validate(resetPasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it has no uppercase letter");
    }

    @Test
    public void newPasswordMustContainALowercaseLetter() {
        resetPasswordRequest.setToken("token-123");
        resetPasswordRequest.setNewPassword("UPPERCASE123");
        Boolean hasViolation = hasViolationOn(validator.validate(resetPasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it has no lowercase letter");
    }

    @Test
    public void newPasswordMustContainADigit() {
        resetPasswordRequest.setToken("token-123");
        resetPasswordRequest.setNewPassword("NoDigitsHere");
        Boolean hasViolation = hasViolationOn(validator.validate(resetPasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it has no digit");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<ResetPasswordRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}