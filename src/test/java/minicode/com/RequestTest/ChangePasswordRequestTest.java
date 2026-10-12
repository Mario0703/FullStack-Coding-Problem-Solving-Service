package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.Request.ChangePasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangePasswordRequestTest {

    private ChangePasswordRequest changePasswordRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.changePasswordRequest = new ChangePasswordRequest();
    }

    @Test
    public void validChangePasswordRequestHasNoViolations(){
        changePasswordRequest.setCurrentPassword("OldPassword123");
        changePasswordRequest.setNewPassword("NewPassword123");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void currentPasswordCannotBeNull(){
        changePasswordRequest.setCurrentPassword(null);
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "currentPassword");
        assertTrue(hasViolation, "Expected violation on currentPassword when it is null");
    }

    @Test
    public void currentPasswordCannotBeBlank(){
        changePasswordRequest.setCurrentPassword("");
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "currentPassword");
        assertTrue(hasViolation, "Expected violation on currentPassword when it is blank");
    }

    @Test
    public void newPasswordCannotBeNull(){
        changePasswordRequest.setNewPassword(null);
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it is null");
    }

    @Test
    public void newPasswordCannotBeBlank(){
        changePasswordRequest.setNewPassword("");
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it is blank");
    }

    @Test
    public void newPasswordCannotBeShorterThanMinLength(){
        changePasswordRequest.setNewPassword("Ab1");
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it is shorter than 8 characters");
    }

    @Test
    public void newPasswordCannotExceedMaxLength(){
        changePasswordRequest.setNewPassword("Aa1" + "a".repeat(70));
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it exceeds 72 characters");
    }

    @Test
    public void newPasswordMustContainAnUppercaseLetter(){
        changePasswordRequest.setNewPassword("lowercase123");
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it has no uppercase letter");
    }

    @Test
    public void newPasswordMustContainALowercaseLetter(){
        changePasswordRequest.setNewPassword("UPPERCASE123");
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it has no lowercase letter");
    }

    @Test
    public void newPasswordMustContainADigit(){
        changePasswordRequest.setNewPassword("NoDigitsHere");
        Boolean hasViolation = hasViolationOn(validator.validate(changePasswordRequest), "newPassword");
        assertTrue(hasViolation, "Expected violation on newPassword when it has no digit");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<ChangePasswordRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
