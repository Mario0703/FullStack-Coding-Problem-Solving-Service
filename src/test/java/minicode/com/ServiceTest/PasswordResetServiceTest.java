package minicode.com.ServiceTest;

import minicode.com.dto.User.Request.ResetPasswordRequest;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import minicode.com.service.User.PasswordResetMailSender;
import minicode.com.service.User.PasswordResetService;
import minicode.com.service.User.ResetTokenStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PasswordResetServiceTest {

    private static final String EMAIL = "user@example.com";
    private static final String TOKEN = "token-123";
    private static final String NEW_PASSWORD = "NewPassword123";
    private static final String ENCODED_PASSWORD = "encoded-password";

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private ResetTokenStore tokenStore;
    private PasswordResetMailSender resetMailSender;
    private PasswordResetService passwordResetService;
    private User user;

    @BeforeEach
    public void setUp() {
        userRepo = mock(UserRepo.class);
        passwordEncoder = mock(PasswordEncoder.class);
        tokenStore = mock(ResetTokenStore.class);
        resetMailSender = mock(PasswordResetMailSender.class);
        passwordResetService = new PasswordResetService(
                userRepo, passwordEncoder, tokenStore, resetMailSender);

        user = new User();
        user.setUserName("tester");
        user.setEmail(EMAIL);
        user.setPassword("old-encoded");
    }

    @Test
    public void givenARegisteredEmailWhenAPasswordResetIsRequestedThenATokenIsIssuedAndTheResetMailIsSent() {
        // given a registered email
        when(userRepo.findFirstByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(tokenStore.issueFor(EMAIL)).thenReturn(TOKEN);

        // when a password reset is requested for it
        passwordResetService.requestPasswordReset(EMAIL);

        // then a timed token is issued and the reset mail is sent to that user
        verify(resetMailSender).sendResetLink(EMAIL, TOKEN);
    }

    @Test
    public void givenAnUnknownEmailWhenAPasswordResetIsRequestedThenNothingIsSentAndNoErrorIsRevealed() {
        // given an email no account is registered with
        when(userRepo.findFirstByEmail(EMAIL)).thenReturn(Optional.empty());

        // when a password reset is requested for it
        assertDoesNotThrow(() -> passwordResetService.requestPasswordReset(EMAIL));

        // then no token is issued, no mail goes out and the caller learns nothing
        verify(tokenStore, never()).issueFor(anyString());
        verify(resetMailSender, never()).sendResetLink(anyString(), anyString());
    }

    @Test
    public void givenAValidTokenWhenThePasswordIsResetThenTheUserPasswordIsUpdated() {
        // given a valid reset token for a registered user
        when(tokenStore.consume(TOKEN)).thenReturn(Optional.of(EMAIL));
        when(userRepo.findFirstByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        // when the password is reset with it
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken(TOKEN);
        request.setNewPassword(NEW_PASSWORD);
        passwordResetService.resetPassword(request);

        // then the user's password is the encoded new password and it is saved
        assertEquals(ENCODED_PASSWORD, user.getPassword());
        verify(userRepo).save(user);
    }

    @Test
    public void givenAnExpiredTokenWhenThePasswordIsResetThenTheResetIsRejected() {
        // given a token that expired or never existed
        when(tokenStore.consume(TOKEN)).thenReturn(Optional.empty());

        // when the password is reset with it
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken(TOKEN);
        request.setNewPassword(NEW_PASSWORD);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> passwordResetService.resetPassword(request)
        );

        // then the reset is rejected and nothing is written
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void givenATokenForAnAccountThatNoLongerExistsWhenThePasswordIsResetThenTheResetIsRejected() {
        // given a token whose account was deleted meanwhile
        when(tokenStore.consume(TOKEN)).thenReturn(Optional.of(EMAIL));
        when(userRepo.findFirstByEmail(EMAIL)).thenReturn(Optional.empty());

        // when the password is reset with it
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken(TOKEN);
        request.setNewPassword(NEW_PASSWORD);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> passwordResetService.resetPassword(request)
        );

        // then the reset is rejected and nothing is written
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(userRepo, never()).save(any());
    }
}