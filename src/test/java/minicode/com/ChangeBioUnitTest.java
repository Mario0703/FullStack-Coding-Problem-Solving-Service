package minicode.com;

import minicode.com.dto.User.Request.ChangeBioRequest;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.RoleRepo;
import minicode.com.repository.UserRepo;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChangeBioUnitTest {

    private UserService userServiceWith(UserRepo userRepo) {
        PasswordEncoder passwordEncoder = new minicode.com.security.BcryptPasswordEncoder();
        RoleRepo roleRepo = mock(RoleRepo.class);
        return new UserService(userRepo, passwordEncoder, roleRepo);
    }

    private User user(UUID userId) {
        User user = new User();
        user.setUserName("BioUser");
        user.setEmail("bio@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setUserId(userId);
        return user;
    }

    @Test
    public void changeBioSavesTrimmedBio() {
        UUID userId = UUID.randomUUID();
        User user = user(userId);

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChangeBioRequest request = new ChangeBioRequest();
        request.setNewBio("  I enjoy binary trees.  ");

        User saved = userServiceWith(mockUserRepo).changeBio(userId, request);

        assertEquals("I enjoy binary trees.", saved.getBio());
    }

    @Test
    public void changeBioClearsBioWhenBlank() {
        UUID userId = UUID.randomUUID();
        User user = user(userId);
        user.setBio("Old bio");

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChangeBioRequest request = new ChangeBioRequest();
        request.setNewBio("   ");

        User saved = userServiceWith(mockUserRepo).changeBio(userId, request);

        assertNull(saved.getBio());
    }

    @Test
    public void changeBioRejectsUnknownUser() {
        UUID userId = UUID.randomUUID();

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.empty());

        ChangeBioRequest request = new ChangeBioRequest();
        request.setNewBio("A bio");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userServiceWith(mockUserRepo).changeBio(userId, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}