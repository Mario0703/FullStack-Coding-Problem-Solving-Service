package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.Challenge.Request.CreateChallengeRequest;
import minicode.com.dto.Challenge.Request.CreateExamplesRequest;
import minicode.com.dto.Challenge.Request.CreateLanguageSetupRequest;
import minicode.com.dto.Challenge.Request.CreateTestCaseRequest;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateChallengeRequestTest {

    private CreateChallengeRequest createChallengeRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.createChallengeRequest = new CreateChallengeRequest();
    }

    @Test
    public void validCreateChallengeRequestHasNoViolations(){
        populateValidRequest();
        Set<ConstraintViolation<CreateChallengeRequest>> violations = validator.validate(createChallengeRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void hintsAreOptional(){
        populateValidRequest();
        createChallengeRequest.setHints(null);
        Set<ConstraintViolation<CreateChallengeRequest>> violations = validator.validate(createChallengeRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void languageSetupsAreOptional(){
        populateValidRequest();
        createChallengeRequest.setLanguageSetups(null);
        Set<ConstraintViolation<CreateChallengeRequest>> violations = validator.validate(createChallengeRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void nameCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setName(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "name"),
                "Expected violation on name when it is null");
    }

    @Test
    public void nameCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setName("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "name"),
                "Expected violation on name when it is blank");
    }

    @Test
    public void nameCannotBeShorterThanMinLength(){
        populateValidRequest();
        createChallengeRequest.setName("ab");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "name"),
                "Expected violation on name when it is shorter than 3 characters");
    }

    @Test
    public void nameCannotExceedMaxLength(){
        populateValidRequest();
        createChallengeRequest.setName("a".repeat(51));
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "name"),
                "Expected violation on name when it exceeds 50 characters");
    }

    @Test
    public void descriptionCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setDescription(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "description"),
                "Expected violation on description when it is null");
    }

    @Test
    public void descriptionCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setDescription("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "description"),
                "Expected violation on description when it is blank");
    }

    @Test
    public void descriptionCannotBeShorterThanMinLength(){
        populateValidRequest();
        createChallengeRequest.setDescription("ab");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "description"),
                "Expected violation on description when it is shorter than 3 characters");
    }

    @Test
    public void descriptionCannotExceedMaxLength(){
        populateValidRequest();
        createChallengeRequest.setDescription("a".repeat(501));
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "description"),
                "Expected violation on description when it exceeds 500 characters");
    }

    @Test
    public void difficultyCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setDifficulty(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "difficulty"),
                "Expected violation on difficulty when it is null");
    }

    @Test
    public void categoryCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setCategory(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "category"),
                "Expected violation on category when it is null");
    }

    @Test
    public void solutionCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setSolution(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "solution"),
                "Expected violation on solution when it is null");
    }

    @Test
    public void solutionCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setSolution("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "solution"),
                "Expected violation on solution when it is blank");
    }

    @Test
    public void templateCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setTemplate(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "template"),
                "Expected violation on template when it is null");
    }

    @Test
    public void templateCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setTemplate("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "template"),
                "Expected violation on template when it is blank");
    }

    @Test
    public void templateExplanationCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setTemplateExplanation(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "templateExplanation"),
                "Expected violation on templateExplanation when it is null");
    }

    @Test
    public void templateExplanationCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setTemplateExplanation("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "templateExplanation"),
                "Expected violation on templateExplanation when it is blank");
    }

    @Test
    public void runnerCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setRunner(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "runner"),
                "Expected violation on runner when it is null");
    }

    @Test
    public void runnerCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setRunner("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "runner"),
                "Expected violation on runner when it is blank");
    }

    @Test
    public void runnerExplanationCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setRunnerExplanation(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "runnerExplanation"),
                "Expected violation on runnerExplanation when it is null");
    }

    @Test
    public void runnerExplanationCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setRunnerExplanation("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "runnerExplanation"),
                "Expected violation on runnerExplanation when it is blank");
    }

    @Test
    public void languageCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setLanguage(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "language"),
                "Expected violation on language when it is null");
    }

    @Test
    public void languageCannotBeBlank(){
        populateValidRequest();
        createChallengeRequest.setLanguage("");
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "language"),
                "Expected violation on language when it is blank");
    }

    @Test
    public void testCasesCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setTestCases(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "testCases"),
                "Expected violation on testCases when it is null");
    }

    @Test
    public void testCasesCannotBeEmpty(){
        populateValidRequest();
        createChallengeRequest.setTestCases(List.of());
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "testCases"),
                "Expected violation on testCases when it is empty");
    }

    @Test
    public void examplesCannotBeNull(){
        populateValidRequest();
        createChallengeRequest.setExamples(null);
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "examples"),
                "Expected violation on examples when it is null");
    }

    @Test
    public void examplesCannotBeEmpty(){
        populateValidRequest();
        createChallengeRequest.setExamples(List.of());
        assertTrue(hasViolationOn(validator.validate(createChallengeRequest), "examples"),
                "Expected violation on examples when it is empty");
    }

    @Test
    public void nestedInvalidTestCaseIsRejected(){
        populateValidRequest();
        CreateTestCaseRequest testCase = new CreateTestCaseRequest();
        testCase.setInput("");
        testCase.setExpectedOutput("3");
        createChallengeRequest.setTestCases(List.of(testCase));
        assertTrue(hasViolationOnPathPrefix(validator.validate(createChallengeRequest), "testCases"),
                "Expected violation for an invalid nested test case");
    }

    @Test
    public void nestedInvalidExampleIsRejected(){
        populateValidRequest();
        CreateExamplesRequest example = new CreateExamplesRequest();
        example.setInputExample("1 2");
        example.setOutputExample(null);
        createChallengeRequest.setExamples(List.of(example));
        assertTrue(hasViolationOnPathPrefix(validator.validate(createChallengeRequest), "examples"),
                "Expected violation for an invalid nested example");
    }

    @Test
    public void nestedInvalidLanguageSetupIsRejected(){
        populateValidRequest();
        CreateLanguageSetupRequest setup = new CreateLanguageSetupRequest();
        setup.setLanguage("python");
        setup.setTemplate("def solve(): pass");
        setup.setTemplateExplanation("Implement solve");
        setup.setRunner("");
        setup.setRunnerExplanation("Runs the tests");
        createChallengeRequest.setLanguageSetups(List.of(setup));
        assertTrue(hasViolationOnPathPrefix(validator.validate(createChallengeRequest), "languageSetups"),
                "Expected violation for an invalid nested language setup");
    }

    private void populateValidRequest(){
        createChallengeRequest.setName("Two Sum");
        createChallengeRequest.setDescription("Return the indices of the two numbers that add up to the target.");
        createChallengeRequest.setDifficulty(Difficulty.EASY);
        createChallengeRequest.setCategory(Category.ARRAYS);
        createChallengeRequest.setSolution("def two_sum(nums, target): pass");
        createChallengeRequest.setTemplate("def two_sum(nums, target): pass");
        createChallengeRequest.setTemplateExplanation("Implement two_sum");
        createChallengeRequest.setRunner("python main.py");
        createChallengeRequest.setRunnerExplanation("Runs the tests");
        createChallengeRequest.setLanguage("python");
        createChallengeRequest.setTestCases(List.of(validTestCase()));
        createChallengeRequest.setExamples(List.of(validExample()));
    }

    private static CreateTestCaseRequest validTestCase(){
        CreateTestCaseRequest testCase = new CreateTestCaseRequest();
        testCase.setInput("2 7 11 15, 9");
        testCase.setExpectedOutput("0 1");
        return testCase;
    }

    private static CreateExamplesRequest validExample(){
        CreateExamplesRequest example = new CreateExamplesRequest();
        example.setInputExample("2 7 11 15, 9");
        example.setOutputExample("0 1");
        return example;
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<CreateChallengeRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }

    private static boolean hasViolationOnPathPrefix(
            Set<ConstraintViolation<CreateChallengeRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().startsWith(field));
    }
}
