package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.Challenge.Request.CreateLanguageSetupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateLanguageSetupRequestTest {

    private CreateLanguageSetupRequest createLanguageSetupRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.createLanguageSetupRequest = new CreateLanguageSetupRequest();
    }

    @Test
    public void validCreateLanguageSetupRequestHasNoViolations(){
        populateValidRequest();
        Set<ConstraintViolation<CreateLanguageSetupRequest>> violations = validator.validate(createLanguageSetupRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void languageCannotBeNull(){
        populateValidRequest();
        createLanguageSetupRequest.setLanguage(null);
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "language"),
                "Expected violation on language when it is null");
    }

    @Test
    public void languageCannotBeBlank(){
        populateValidRequest();
        createLanguageSetupRequest.setLanguage("");
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "language"),
                "Expected violation on language when it is blank");
    }

    @Test
    public void templateCannotBeNull(){
        populateValidRequest();
        createLanguageSetupRequest.setTemplate(null);
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "template"),
                "Expected violation on template when it is null");
    }

    @Test
    public void templateCannotBeBlank(){
        populateValidRequest();
        createLanguageSetupRequest.setTemplate("");
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "template"),
                "Expected violation on template when it is blank");
    }

    @Test
    public void templateExplanationCannotBeNull(){
        populateValidRequest();
        createLanguageSetupRequest.setTemplateExplanation(null);
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "templateExplanation"),
                "Expected violation on templateExplanation when it is null");
    }

    @Test
    public void templateExplanationCannotBeBlank(){
        populateValidRequest();
        createLanguageSetupRequest.setTemplateExplanation("");
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "templateExplanation"),
                "Expected violation on templateExplanation when it is blank");
    }

    @Test
    public void runnerCannotBeNull(){
        populateValidRequest();
        createLanguageSetupRequest.setRunner(null);
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "runner"),
                "Expected violation on runner when it is null");
    }

    @Test
    public void runnerCannotBeBlank(){
        populateValidRequest();
        createLanguageSetupRequest.setRunner("");
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "runner"),
                "Expected violation on runner when it is blank");
    }

    @Test
    public void runnerExplanationCannotBeNull(){
        populateValidRequest();
        createLanguageSetupRequest.setRunnerExplanation(null);
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "runnerExplanation"),
                "Expected violation on runnerExplanation when it is null");
    }

    @Test
    public void runnerExplanationCannotBeBlank(){
        populateValidRequest();
        createLanguageSetupRequest.setRunnerExplanation("");
        assertTrue(hasViolationOn(validator.validate(createLanguageSetupRequest), "runnerExplanation"),
                "Expected violation on runnerExplanation when it is blank");
    }

    private void populateValidRequest(){
        createLanguageSetupRequest.setLanguage("python");
        createLanguageSetupRequest.setTemplate("def solve(): pass");
        createLanguageSetupRequest.setTemplateExplanation("Implement solve");
        createLanguageSetupRequest.setRunner("python main.py");
        createLanguageSetupRequest.setRunnerExplanation("Runs the tests");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<CreateLanguageSetupRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
