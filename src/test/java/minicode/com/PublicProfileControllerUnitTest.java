package minicode.com;

import minicode.com.controller.User.PublicProfileController;
import minicode.com.dto.User.Response.UserSummaryResponse;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import minicode.com.repository.RoleRepo;
import minicode.com.service.User.UserService;
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

        User user = new User();
        user.setUserName("PublicUser");
        user.setEmail("public@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setUserId(userId);
        user.setBio("I solve bite-sized coding challenges.");
        user.setScore(42);
        user.setNumberOfEasyChallengesSolved(5);
        user.setNumberOfMediumChallengesSolved(3);
        user.setNumberOfHardChallengesSolved(1);

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));

        PasswordEncoder passwordEncoder = new minicode.com.security.BcryptPasswordEncoder();
        RoleRepo roleRepo = mock(RoleRepo.class);
        UserService userService = new UserService(mockUserRepo, passwordEncoder, roleRepo);

        PublicProfileController controller = new PublicProfileController(userService);
        UserSummaryResponse summary = controller.getPublicProfile(userId);

        assertEquals(userId, summary.userId());
        assertEquals("PublicUser", summary.userName());
        assertEquals("public@example.com", summary.email());
        assertEquals("I solve bite-sized coding challenges.", summary.bio());
        assertEquals(42, summary.score());
        assertEquals(5, summary.numberOfEasyChallengesSolved());
        assertEquals(3, summary.numberOfMediumChallengesSolved());
        assertEquals(1, summary.numberOfHardChallengesSolved());
    }

    @Test
    public void testGetUserBioReturnsBioByUserId() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setUserName("PublicUser");
        user.setEmail("public@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setUserId(userId);

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.findBioById(userId)).thenReturn(Optional.of("Bio found by id"));

        PasswordEncoder passwordEncoder = new minicode.com.security.BcryptPasswordEncoder();
        RoleRepo roleRepo = mock(RoleRepo.class);
        UserService userService = new UserService(mockUserRepo, passwordEncoder, roleRepo);

        PublicProfileController controller = new PublicProfileController(userService);

        assertEquals("Bio found by id", controller.getUserBio(userId).bio());
    }
}
