package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.Challenge.Request.CreateTestCaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateTestCaseRequestTest {

    private CreateTestCaseRequest createTestCaseRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.createTestCaseRequest = new CreateTestCaseRequest();
    }

    @Test
    public void validCreateTestCaseRequestHasNoViolations(){
        createTestCaseRequest.setInput("1 2");
        createTestCaseRequest.setExpectedOutput("3");
        Set<ConstraintViolation<CreateTestCaseRequest>> violations = validator.validate(createTestCaseRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void inputCannotBeNull(){
        createTestCaseRequest.setInput(null);
        Boolean hasViolation = hasViolationOn(validator.validate(createTestCaseRequest), "input");
        assertTrue(hasViolation, "Expected violation on input when it is null");
    }

    @Test
    public void inputCannotBeBlank(){
        createTestCaseRequest.setInput("");
        Boolean hasViolation = hasViolationOn(validator.validate(createTestCaseRequest), "input");
        assertTrue(hasViolation, "Expected violation on input when it is blank");
    }

    @Test
    public void expectedOutputCannotBeNull(){
        createTestCaseRequest.setExpectedOutput(null);
        Boolean hasViolation = hasViolationOn(validator.validate(createTestCaseRequest), "expectedOutput");
        assertTrue(hasViolation, "Expected violation on expectedOutput when it is null");
    }

    @Test
    public void expectedOutputCannotBeBlank(){
        createTestCaseRequest.setExpectedOutput("");
        Boolean hasViolation = hasViolationOn(validator.validate(createTestCaseRequest), "expectedOutput");
        assertTrue(hasViolation, "Expected violation on expectedOutput when it is blank");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<CreateTestCaseRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
