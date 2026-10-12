package minicode.com.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field-level constraint for the staff role of a staff creation request:
 * only the roles MiniLeetCode counts as staff (admin, moderator) are
 * accepted, never the regular user or owner roles.
 */
@Documented
@Constraint(validatedBy = StaffRoleValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStaffRole {

    String message() default "Only ADMIN and MODERATOR staff can be created";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}