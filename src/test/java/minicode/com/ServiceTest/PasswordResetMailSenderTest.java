package minicode.com.ServiceTest;

import minicode.com.service.User.PasswordResetMailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class PasswordResetMailSenderTest {

    private static final String FROM_ADDRESS = "noreply@minileetcode.com";
    private static final String FRONTEND_URL = "http://localhost:5173";
    private static final String MAIL_HOST = "smtp.example.com";
    private static final String EMAIL = "user@example.com";
    private static final String TOKEN = "token-123";

    private JavaMailSender javaMailSender;
    private PasswordResetMailSender resetMailSender;

    @BeforeEach
    public void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        resetMailSender = new PasswordResetMailSender(
                javaMailSender, FROM_ADDRESS, FRONTEND_URL, MAIL_HOST);
    }

    @Test
    public void givenAUserWithATokenWhenTheResetMailIsSentThenTheMailLeadsToTheResetPageAndMentionsTheExpiry() {
        // given a user with a reset token

        // when the reset mail is sent
        resetMailSender.sendResetLink(EMAIL, TOKEN);

        // then the mail is addressed to the user and contains the timed reset link
        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(mailCaptor.capture());

        SimpleMailMessage mail = mailCaptor.getValue();
        assertArrayEquals(new String[]{EMAIL}, mail.getTo());
        assertEquals(FROM_ADDRESS, mail.getFrom());
        assertTrue(mail.getText().contains(FRONTEND_URL + "/reset-password?token=" + TOKEN));
        assertTrue(mail.getText().contains("5 minutes"));
    }

    @Test
    public void givenNoMailServerIsConfiguredWhenTheResetMailIsSentThenTheRequestIsRejectedAsServiceUnavailable() {
        // given no mail server is configured
        PasswordResetMailSender unconfiguredSender = new PasswordResetMailSender(
                javaMailSender, FROM_ADDRESS, FRONTEND_URL, "");

        // when the reset mail is sent
        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> unconfiguredSender.sendResetLink(EMAIL, TOKEN)
        );

        // then the request is rejected as service unavailable and nothing is sent
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void givenTheMailServerFailsToSendWhenTheResetMailIsSentThenTheRequestFailsWithAFriendlyError() {
        // given the mail server rejects the message
        doThrow(new MailSendException("connection refused")).when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        // when the reset mail is sent
        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> resetMailSender.sendResetLink(EMAIL, TOKEN)
        );

        // then the request fails with a friendly error instead of a stack trace
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getStatusCode());
        assertEquals("Unable to send the password reset email. Please try again later.",
                error.getReason());
    }
}