package minicode.com.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level constraint for a login request that must carry exactly one
 * identifier: either an email or a username, never both and never neither.
 */
@Documented
@Constraint(validatedBy = EitherEmailOrUserNameValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EitherEmailOrUserName {

    String message() default "Provide exactly one of email or username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
