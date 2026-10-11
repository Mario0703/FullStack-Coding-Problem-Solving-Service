package minicode.com.RequestTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.SignupRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignupRequestTest {

    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validSignupRequestHasNoViolations() {
        SignupRequest request = new SignupRequest();
        request.setUserName("tester");
        request.setEmail("tester@example.com");
        request.setPassword("ThisIsAValidPassword123!");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void insecurePasswordIsRejected() {
        SignupRequest request = new SignupRequest();
        request.setUserName("tester");
        request.setEmail("tester@example.com");
        request.setPassword("short");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(hasViolationOn(violations, "password"));
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<SignupRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
