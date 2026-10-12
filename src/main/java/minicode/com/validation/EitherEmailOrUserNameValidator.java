package minicode.com.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import minicode.com.dto.User.Request.LoginRequest;

public class EitherEmailOrUserNameValidator
        implements ConstraintValidator<EitherEmailOrUserName, LoginRequest> {

    @Override
    public boolean isValid(LoginRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        boolean hasEmail = isPresent(request.getEmail());
        boolean hasUserName = isPresent(request.getUserName());

        return hasEmail ^ hasUserName;
    }

    private boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }
}
