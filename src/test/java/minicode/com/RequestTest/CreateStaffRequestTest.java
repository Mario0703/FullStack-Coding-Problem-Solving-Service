package minicode.com.RequestTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import minicode.com.dto.Admin.CreateStaffRequest;
import minicode.com.entity.Roles.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateStaffRequestTest {

    private CreateStaffRequest createStaffRequest;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.createStaffRequest = new CreateStaffRequest();
        this.createStaffRequest.setUserName("StaffUser");
        this.createStaffRequest.setEmail("staff@example.com");
        this.createStaffRequest.setPassword("Password123");
        this.createStaffRequest.setStaffRole(UserRole.ADMIN);
    }

    @Test
    public void validAdminStaffRequestHasNoViolations() {
        createStaffRequest.setStaffRole(UserRole.ADMIN);
        Set<ConstraintViolation<CreateStaffRequest>> violations = validator.validate(createStaffRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void validModeratorStaffRequestHasNoViolations() {
        createStaffRequest.setStaffRole(UserRole.MODERATOR);
        Set<ConstraintViolation<CreateStaffRequest>> violations = validator.validate(createStaffRequest);
        assertTrue(violations.isEmpty(), () -> "unexpected violations: " + violations);
    }

    @Test
    public void staffRoleCannotBeNull() {
        createStaffRequest.setStaffRole(null);
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "staffRole");
        assertTrue(hasViolation, "Expected violation on staffRole when it is null");
    }

    @Test
    public void staffRoleCannotBeARegularUserRole() {
        createStaffRequest.setStaffRole(UserRole.USER);
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "staffRole");
        assertTrue(hasViolation, "Expected violation on staffRole when it is the regular user role");
    }

    @Test
    public void staffRoleCannotBeTheOwnerRole() {
        createStaffRequest.setStaffRole(UserRole.OWNER);
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "staffRole");
        assertTrue(hasViolation, "Expected violation on staffRole when it is the owner role");
    }

    @Test
    public void userNameCannotBeBlank() {
        createStaffRequest.setUserName("");
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "userName");
        assertTrue(hasViolation, "Expected violation on userName when it is blank");
    }

    @Test
    public void userNameCannotBeShorterThanMinLength() {
        createStaffRequest.setUserName("abcd");
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "userName");
        assertTrue(hasViolation, "Expected violation on userName when it is shorter than 5 characters");
    }

    @Test
    public void emailCannotBeBlank() {
        createStaffRequest.setEmail("");
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "email");
        assertTrue(hasViolation, "Expected violation on email when it is blank");
    }

    @Test
    public void emailMustBeAValidAddress() {
        createStaffRequest.setEmail("not-an-email");
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "email");
        assertTrue(hasViolation, "Expected violation on email when it is not a valid address");
    }

    @Test
    public void passwordCannotBeBlank() {
        createStaffRequest.setPassword("");
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "password");
        assertTrue(hasViolation, "Expected violation on password when it is blank");
    }

    @Test
    public void passwordMustContainAnUppercaseLetterALowercaseLetterAndADigit() {
        createStaffRequest.setPassword("weakpassword");
        Boolean hasViolation = hasViolationOn(validator.validate(createStaffRequest), "password");
        assertTrue(hasViolation, "Expected violation on password when it misses a required character type");
    }

    private static boolean hasViolationOn(
            Set<ConstraintViolation<CreateStaffRequest>> violations, String field) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}