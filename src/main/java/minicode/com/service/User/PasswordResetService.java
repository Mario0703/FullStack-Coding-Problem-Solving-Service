package minicode.com.service.User;

import jakarta.transaction.Transactional;
import minicode.com.dto.User.Request.ResetPasswordRequest;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * The forgot-password flow: issuing the timed reset link by mail and
 * completing the reset with the token from that link. A request for an
 * unknown email succeeds silently so outsiders cannot probe which
 * addresses are registered.
 */
@Service
public class PasswordResetService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ResetTokenStore tokenStore;
    private final PasswordResetMailSender resetMailSender;

    public PasswordResetService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            ResetTokenStore tokenStore,
            PasswordResetMailSender resetMailSender
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.resetMailSender = resetMailSender;
    }

    public void requestPasswordReset(String email) {
        Optional<User> user = userRepo.findFirstByEmail(email);
        if (user.isEmpty()) {
            return;
        }

        String token = tokenStore.issueFor(email);
        resetMailSender.sendResetLink(user.get().getEmail(), token);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String email = tokenStore.consume(request.getToken())
                .orElseThrow(this::invalidTokenError);

        User user = userRepo.findFirstByEmail(email)
                .orElseThrow(this::invalidTokenError);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }

    private ResponseStatusException invalidTokenError() {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "This password reset link is invalid or has expired. Please request a new one."
        );
    }
}