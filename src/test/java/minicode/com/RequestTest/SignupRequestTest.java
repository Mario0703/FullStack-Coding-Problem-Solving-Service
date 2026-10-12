package minicode.com.RequestTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignupRequestTest {

    private Validator validator;
    private SignupRequest request;

    @BeforeEach
     void createValidator() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.request = new SignupRequest();
    }

    @Test
    public void validSignupRequestHasNoViolations() {
        request.setUserName("tester");
        request.setEmail("tester@example.com");
        request.setPassword("ThisIsAValidPassword123!");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void insecurePasswordIsRejected() {
        request.setUserName("tester");
        request.setEmail("tester@example.com");
        request.setPassword("short");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(hasViolationOn(violations, "password"));
    }

    @Test
    public void invalidEmailIsRejected(){
        request.setUserName("tester");
        request.setEmail("invalidmail");
        request.setPassword("thisIsAValidPassword123!");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(hasViolationOn(violations, "email"));
    }
    @Test
    public void invalidUserNameOfMinimalLengthIsRejected(){
        request.setUserName("t");
        request.setEmail("invalidmail");
        request.setPassword("thisIsAValidPassword123!");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(hasViolationOn(violations, "userName"));
    }
    @Test
    public void userNameCannotExceedMaxLenght(){
        String userName = "a".repeat(101);
        request.setUserName(userName);
        request.setEmail("exampleMail");
        request.setPassword("thisIsAValidPassword123!");
        assertTrue(hasViolationOn(validator.validate(request), "userName"));
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<SignupRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
