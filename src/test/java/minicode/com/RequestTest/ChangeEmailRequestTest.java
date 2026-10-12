package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.Request.ChangeEmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeEmailRequestTest {

    private ChangeEmailRequest changeEmailRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.changeEmailRequest = new ChangeEmailRequest();
    }

    @Test
    public void validChangeEmailRequestHasNoViolations(){
        changeEmailRequest.setNewEmail("tester@example.com");
        Set<ConstraintViolation<ChangeEmailRequest>> violations = validator.validate(changeEmailRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void newEmailCannotBeNull(){
        changeEmailRequest.setNewEmail(null);
        Boolean hasViolation = hasViolationOn(validator.validate(changeEmailRequest), "newEmail");
        assertTrue(hasViolation, "Expected violation on newEmail when it is null");
    }

    @Test
    public void newEmailCannotBeBlank(){
        changeEmailRequest.setNewEmail("");
        Boolean hasViolation = hasViolationOn(validator.validate(changeEmailRequest), "newEmail");
        assertTrue(hasViolation, "Expected violation on newEmail when it is blank");
    }

    @Test
    public void newEmailMustBeValid(){
        changeEmailRequest.setNewEmail("invalid");
        Boolean hasViolation = hasViolationOn(validator.validate(changeEmailRequest), "newEmail");
        assertTrue(hasViolation, "Expected violation on newEmail when it is not a valid email");
    }

    @Test
    public void newEmailCannotExceedMaxLength(){
        changeEmailRequest.setNewEmail("a".repeat(101) + "@example.com");
        Boolean hasViolation = hasViolationOn(validator.validate(changeEmailRequest), "newEmail");
        assertTrue(hasViolation, "Expected violation on newEmail when it exceeds 100 characters");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<ChangeEmailRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
