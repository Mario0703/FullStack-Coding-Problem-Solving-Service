package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.Challenge.Request.CreateExamplesRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateExamplesRequestTest {

    private CreateExamplesRequest createExamplesRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.createExamplesRequest = new CreateExamplesRequest();
    }

    @Test
    public void validCreateExamplesRequestHasNoViolations(){
        createExamplesRequest.setInputExample("1 2");
        createExamplesRequest.setOutputExample("3");
        Set<ConstraintViolation<CreateExamplesRequest>> violations = validator.validate(createExamplesRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void inputExampleCannotBeNull(){
        createExamplesRequest.setInputExample(null);
        Boolean hasViolation = hasViolationOn(validator.validate(createExamplesRequest), "inputExample");
        assertTrue(hasViolation, "Expected violation on inputExample when it is null");
    }

    @Test
    public void inputExampleCannotBeBlank(){
        createExamplesRequest.setInputExample("");
        Boolean hasViolation = hasViolationOn(validator.validate(createExamplesRequest), "inputExample");
        assertTrue(hasViolation, "Expected violation on inputExample when it is blank");
    }

    @Test
    public void outputExampleCannotBeNull(){
        createExamplesRequest.setOutputExample(null);
        Boolean hasViolation = hasViolationOn(validator.validate(createExamplesRequest), "outputExample");
        assertTrue(hasViolation, "Expected violation on outputExample when it is null");
    }

    @Test
    public void outputExampleCannotBeBlank(){
        createExamplesRequest.setOutputExample("");
        Boolean hasViolation = hasViolationOn(validator.validate(createExamplesRequest), "outputExample");
        assertTrue(hasViolation, "Expected violation on outputExample when it is blank");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<CreateExamplesRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
