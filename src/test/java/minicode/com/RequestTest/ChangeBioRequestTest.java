package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.Request.ChangeBioRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeBioRequestTest {

    private ChangeBioRequest changeBioRequest;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.changeBioRequest = new ChangeBioRequest();
    }

    @Test
    public void validChangeBioRequestHasNoViolations() {
        changeBioRequest.setNewBio("I practice algorithms every day.");
        Set<ConstraintViolation<ChangeBioRequest>> violations = validator.validate(changeBioRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void nullBioIsAllowedBecauseItClearsTheBio() {
        changeBioRequest.setNewBio(null);
        Set<ConstraintViolation<ChangeBioRequest>> violations = validator.validate(changeBioRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void blankBioIsAllowedBecauseItClearsTheBio() {
        changeBioRequest.setNewBio("   ");
        Set<ConstraintViolation<ChangeBioRequest>> violations = validator.validate(changeBioRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void bioCannotExceedMaxLength() {
        changeBioRequest.setNewBio("a".repeat(501));
        Boolean hasViolation = hasViolationOn(validator.validate(changeBioRequest), "newBio");
        assertTrue(hasViolation, "Expected violation on newBio when it exceeds 500 characters");
    }

    @Test
    public void bioAtMaxLengthIsValid() {
        changeBioRequest.setNewBio("a".repeat(500));
        Set<ConstraintViolation<ChangeBioRequest>> violations = validator.validate(changeBioRequest);
        assertFalse(hasViolationOn(violations, "newBio"), "Expected no violation at exactly 500 characters");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<ChangeBioRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}