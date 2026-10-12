package minicode.com;

import minicode.com.dto.User.Request.SignupRequest;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.exception.SignupException;
import minicode.com.repository.UserRepo;
import minicode.com.repository.RoleRepo;
import minicode.com.security.BcryptPasswordEncoder;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.*;


public class MiniLeetCodeSignupUnitTest {

    private UserService createUserService(UserRepo userRepo) {
        PasswordEncoder passwordEncoder = new BcryptPasswordEncoder();
        RoleRepo roleRepo = mock(RoleRepo.class);
        return new UserService(userRepo, passwordEncoder, roleRepo);
    }

    @Test
    public void testUserSignupForAValidUser() {
        String username = "TestUser";
        String email = "testuser@gmail.com";
        String password = "TestPassword123";
        BcryptPasswordEncoder passwordEncoder = new BcryptPasswordEncoder();

        // Creat a signup request
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUserName(username);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);

        // Create a mock UserRepo, and save the user to the mock repo
        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findFirstByEmail(email)).thenReturn(Optional.empty());
        when(mockUserRepo.findFirstByUserName(username)).thenReturn(Optional.empty());
        when(mockUserRepo.save(any(User.class))).thenAnswer(
                saveUser -> {
                    int signupRequestArgument = 0;
                    User userToSave = saveUser.getArgument(signupRequestArgument);
                    return userToSave;
                }
        );
        UserService userService = createUserService(mockUserRepo);
        User savedUser = userService.signup(signupRequest);
        // Verify that the user was saved correctly
        assertEquals(username, savedUser.getUserName());
        assertEquals(email, savedUser.getEmail());
        assertTrue(passwordEncoder.matches(password, savedUser.getPassword()));
    }

    @Test
    public void testUserSignupForUsernameWhichAlreadyExists() {
        String userName = "TestUser";
        String email = "TestUser2@example.com";
        String password = "TestPassword123";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUserName(userName);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);

        User existingUser = new User();
        existingUser.setUserName(userName);
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("encoded-password");
        existingUser.setRole(UserRole.USER);

        UserRepo mockUserRepo = mock(UserRepo.class);

        when(mockUserRepo.findFirstByUserName(userName))
                .thenReturn(Optional.of(existingUser));

        UserService userService = createUserService(mockUserRepo);

        SignupException exception = assertThrows(
                SignupException.class,
                () -> userService.signup(signupRequest)
        );

        assertEquals("Username already exists: " + userName, exception.getMessage());

        verify(mockUserRepo, never()).save(any(User.class));
    }
    @Test
    public void testUserSignupForEmailWhichAlreadyExists() {
        String username = "TestUser";
        String email = "existing@example.com";
        String password = "TestPassword123";

        SignupRequest request = new SignupRequest();
        request.setUserName(username);
        request.setEmail(email);
        request.setPassword(password);

        User existingUser = new User();
        existingUser.setUserName("AnotherUser");
        existingUser.setEmail(email);
        existingUser.setPassword("encoded-password");
        existingUser.setRole(UserRole.USER);

        UserRepo mockUserRepo = mock(UserRepo.class);

        when(mockUserRepo.findFirstByUserName(username))
                .thenReturn(Optional.empty());

        when(mockUserRepo.findFirstByEmail(email))
                .thenReturn(Optional.of(existingUser));

        UserService userService = createUserService(mockUserRepo);

        SignupException exception = assertThrows(
                SignupException.class,
                () -> userService.signup(request)
        );

        assertEquals("Email already exists: " + email, exception.getMessage());

        verify(mockUserRepo, never()).save(any(User.class));
    }
}



