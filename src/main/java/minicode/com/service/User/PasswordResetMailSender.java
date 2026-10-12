package minicode.com.service.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Sends the password reset mail. The link inside points at the frontend
 * reset page and only works for five minutes because the token behind it
 * expires (see {@link ResetTokenStore}).
 */
@Service
public class PasswordResetMailSender {

    private static final String RESET_PATH = "/reset-password?token=";
    private static final String MAIL_SUBJECT = "MiniLeetCode password reset";

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String frontendUrl;
    private final String mailHost;

    public PasswordResetMailSender(
            JavaMailSender mailSender,
            @Value("${MAIL_FROM:noreply@minileetcode.com}") String fromAddress,
            @Value("${FRONTEND_URL:http://localhost:5173}") String frontendUrl,
            @Value("${MAIL_HOST:}") String mailHost
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.frontendUrl = frontendUrl;
        this.mailHost = mailHost;
    }

    public void sendResetLink(String toEmail, String token) {
        if (mailHost.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Password reset emails are not configured on this server"
            );
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromAddress);
        mail.setTo(toEmail);
        mail.setSubject(MAIL_SUBJECT);
        mail.setText(mailTextFor(token));

        try {
            mailSender.send(mail);
        } catch (MailException error) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to send the password reset email. Please try again later.",
                    error
            );
        }
    }

    private String mailTextFor(String token) {
        String resetLink = frontendUrl + RESET_PATH + token;

        return String.join("\n",
                "Hello,",
                "",
                "We received a request to reset your MiniLeetCode password.",
                "Open the link below to choose a new password. The link expires in 5 minutes:",
                "",
                resetLink,
                "",
                "If you did not request a password reset, you can safely ignore this email."
        );
    }
}