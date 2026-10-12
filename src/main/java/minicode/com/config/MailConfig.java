package minicode.com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * The SMTP sender for password reset mails. Defined here instead of relying
 * on mail auto-configuration so the bean also exists when no MAIL_HOST is
 * configured (the sender then reports "not configured" instead of the
 * application failing to start).
 */
@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(
            @Value("${MAIL_HOST:}") String host,
            @Value("${MAIL_PORT:587}") int port,
            @Value("${MAIL_USERNAME:}") String username,
            @Value("${MAIL_PASSWORD:}") String password
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(smtpProperties(username));
        return mailSender;
    }

    private Properties smtpProperties(String username) {
        Properties smtpProperties = new Properties();
        if (!username.isBlank()) {
            smtpProperties.put("mail.smtp.auth", "true");
            smtpProperties.put("mail.smtp.starttls.enable", "true");
        }
        return smtpProperties;
    }
}