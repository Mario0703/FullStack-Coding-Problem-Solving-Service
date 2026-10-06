package minicode.com;

import minicode.com.controller.User.PublicProfileController;
import minicode.com.dto.User.UserSummary;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import minicode.com.service.User.UserService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicProfileControllerUnitTest {

    @Test
    public void testGetPublicProfileReturnsUserSummaryWithStats() {
        UUID userId = UUID.randomUUID();

        User user = new User("PublicUser", "public@example.com", "password");
        user.setUserId(userId);
        user.setScore(42);
        user.setNumberOfEasyChallengesSolved(5);
        user.setNumberOfMediumChallengesSolved(3);
        user.setNumberOfHardChallengesSolved(1);

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        PasswordEncoder passwordEncoder = new minicode.com.security.BcryptPasswordEncoder();
        UserService userService = new UserService(mockUserRepo, validator, passwordEncoder);

        PublicProfileController controller = new PublicProfileController(userService);
        UserSummary summary = controller.getPublicProfile(userId);

        assertEquals(userId, summary.userId());
        assertEquals("PublicUser", summary.userName());
        assertEquals("public@example.com", summary.email());
        assertEquals(42, summary.score());
        assertEquals(5, summary.numberOfEasyChallengesSolved());
        assertEquals(3, summary.numberOfMediumChallengesSolved());
        assertEquals(1, summary.numberOfHardChallengesSolved());
    }
}
