package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.Request.ChangeUserNameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeUserNameRequestTest {

    private ChangeUserNameRequest changeUserNameRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.changeUserNameRequest = new ChangeUserNameRequest();
    }

    @Test
    public void validChangeUserNameRequestHasNoViolations(){
        changeUserNameRequest.setNewUserName("tester");
        Set<ConstraintViolation<ChangeUserNameRequest>> violations = validator.validate(changeUserNameRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void newUserNameCannotBeNull(){
        changeUserNameRequest.setNewUserName(null);
        Boolean hasViolation = hasViolationOn(validator.validate(changeUserNameRequest), "newUserName");
        assertTrue(hasViolation, "Expected violation on newUserName when it is null");
    }

    @Test
    public void newUserNameCannotBeBlank(){
        changeUserNameRequest.setNewUserName("");
        Boolean hasViolation = hasViolationOn(validator.validate(changeUserNameRequest), "newUserName");
        assertTrue(hasViolation, "Expected violation on newUserName when it is blank");
    }

    @Test
    public void newUserNameCannotExceedMaxLength(){
        changeUserNameRequest.setNewUserName("a".repeat(101));
        Boolean hasViolation = hasViolationOn(validator.validate(changeUserNameRequest), "newUserName");
        assertTrue(hasViolation, "Expected violation on newUserName when it exceeds 100 characters");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<ChangeUserNameRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
