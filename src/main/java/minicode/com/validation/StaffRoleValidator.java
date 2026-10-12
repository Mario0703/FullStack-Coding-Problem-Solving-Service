package minicode.com.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import minicode.com.entity.Roles.UserRole;

public class StaffRoleValidator implements ConstraintValidator<ValidStaffRole, UserRole> {

    @Override
    public boolean isValid(UserRole staffRole, ConstraintValidatorContext context) {
        if (staffRole == null) {
            return true;
        }

        return staffRole == UserRole.ADMIN || staffRole == UserRole.MODERATOR;
    }
}