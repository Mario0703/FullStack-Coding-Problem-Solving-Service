package minicode.com;

import minicode.com.dto.User.Request.LoginRequest;
import minicode.com.entity.User.User;
import minicode.com.exception.LoginErrorException;
import minicode.com.repository.RoleRepo;
import minicode.com.repository.UserRepo;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserLoginUnitTest {

    private User storedUser(String userName, String email, String encodedPassword) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        return user;
    }

    private UserService userService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        return new UserService(userRepo, passwordEncoder, mock(RoleRepo.class));
    }

    @Test
    public void loginWithUserNameResolvesByUserName() {
        UserRepo userRepo = mock(UserRepo.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        User stored = storedUser("tester", "tester@example.com", "encoded");
        when(userRepo.findFirstByUserName("tester")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("Password123", "encoded")).thenReturn(true);

        LoginRequest request = new LoginRequest();
        request.setUserName("tester");
        request.setPassword("Password123");

        User result = userService(userRepo, passwordEncoder).login(request);

        assertEquals("tester", result.getUserName());
    }

    @Test
    public void loginWithEmailResolvesByEmail() {
        UserRepo userRepo = mock(UserRepo.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        User stored = storedUser("tester", "tester@example.com", "encoded");
        when(userRepo.findFirstByEmail("tester@example.com")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("Password123", "encoded")).thenReturn(true);

        LoginRequest request = new LoginRequest();
        request.setEmail("tester@example.com");
        request.setPassword("Password123");

        User result = userService(userRepo, passwordEncoder).login(request);

        assertEquals("tester", result.getUserName());
    }

    @Test
    public void loginWithWrongPasswordIsRejected() {
        UserRepo userRepo = mock(UserRepo.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        User stored = storedUser("tester", "tester@example.com", "encoded");
        when(userRepo.findFirstByUserName("tester")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("WrongPassword", "encoded")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setUserName("tester");
        request.setPassword("WrongPassword");

        UserService service = userService(userRepo, passwordEncoder);

        assertThrows(LoginErrorException.class, () -> service.login(request));
    }
}
