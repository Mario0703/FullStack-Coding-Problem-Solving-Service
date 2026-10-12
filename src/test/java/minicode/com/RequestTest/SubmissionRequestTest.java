package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.User.SubmissionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.Set;

public class SubmissionRequestTest {

    private SubmissionRequest submissionRequest;
    private Validator validator;

    @BeforeEach
    public void setUp(){
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
       this.submissionRequest = new SubmissionRequest();
    }

    @Test
    public void idCannotBeNull(){
        submissionRequest.setChallengeId(null);
        Boolean hasVioldation = hasViolationOn(validator.validate(submissionRequest), "challengeId");
        assertTrue(hasVioldation, "Expected violation on challengeId when it is null");
    }

    @Test
    public void userIdCannotBeNull(){
        submissionRequest.setUserId(null);
        Boolean hasVioldation = hasViolationOn(validator.validate(submissionRequest), "userId");
        assertTrue(hasVioldation, "Expected violation on userId when it is null");
    }
    @Test
    public void languageCannotBeBlank(){
        submissionRequest.setLanguage("");
        Boolean hasVioldation = hasViolationOn(validator.validate(submissionRequest), "language");
        assertTrue(hasVioldation, "Expected violation on language when it is blank");

    }
    @Test
    public void codeCannotBeBlank(){
        submissionRequest.setCode("");
        Boolean hasVioldation = hasViolationOn(validator.validate(submissionRequest), "code");
        assertTrue(hasVioldation, "Expected violation on code when it is blank");
    }
    @Test
    public void codeCannotBeNull(){
        submissionRequest.setCode(null);
        Boolean hasVioldation = hasViolationOn(validator.validate(submissionRequest), "code");
        assertTrue(hasVioldation, "Expected violation on code when it is null");
    }

    @Test
    public void languageCannotBeNull(){
        submissionRequest.setLanguage(null);
        Boolean hasVioldation = hasViolationOn(validator.validate(submissionRequest), "language");
        assertTrue(hasVioldation, "Expected violation on language when it is null");
    }



    private static boolean hasViolationOn(
            Set<ConstraintViolation<SubmissionRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
