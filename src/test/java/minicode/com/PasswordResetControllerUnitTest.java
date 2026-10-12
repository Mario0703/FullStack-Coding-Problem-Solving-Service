package minicode.com;

import minicode.com.controller.User.UserController;
import minicode.com.dto.User.Request.ForgotPasswordRequest;
import minicode.com.dto.User.Request.ResetPasswordRequest;
import minicode.com.dto.User.Response.PasswordResetResponse;
import minicode.com.service.User.PasswordResetService;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PasswordResetControllerUnitTest {

    @Test
    public void givenAnyValidEmailWhenAPasswordResetIsRequestedThenTheCallerReceivesTheSameNeutralConfirmation() {
        // given any valid email, registered or not
        PasswordResetService passwordResetService = mock(PasswordResetService.class);
        UserController controller = userController(passwordResetService);
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("user@example.com");

        // when the password reset is requested
        PasswordResetResponse response = controller.forgotPassword(request);

        // then the caller receives the same neutral confirmation regardless of the email
        assertEquals("If the email is registered, a password reset link has been sent to it.",
                response.message());
        verify(passwordResetService).requestPasswordReset("user@example.com");
    }

    @Test
    public void givenAValidTokenAndNewPasswordWhenThePasswordIsResetThenTheCallerReceivesAConfirmation() {
        // given a valid reset token and a new password
        PasswordResetService passwordResetService = mock(PasswordResetService.class);
        UserController controller = userController(passwordResetService);
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("token-123");
        request.setNewPassword("NewPassword123");

        // when the password is reset with them
        PasswordResetResponse response = controller.resetPassword(request);

        // then the caller receives a confirmation and the reset is carried out
        assertEquals("Your password has been reset. You can now log in with your new password.",
                response.message());
        verify(passwordResetService).resetPassword(request);
    }

    private UserController userController(PasswordResetService passwordResetService) {
        return new UserController(
                mock(UserService.class),
                passwordResetService,
                mock(AuthenticationManager.class),
                mock(SecurityContextRepository.class)
        );
    }
}