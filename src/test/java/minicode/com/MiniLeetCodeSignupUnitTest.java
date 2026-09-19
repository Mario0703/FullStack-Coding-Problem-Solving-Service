package minicode.com;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;


public class MiniLeetCodeSignupUnitTest {

    private UserService createUserService(UserRepo userRepo) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        PasswordEncoder passwordEncoder = new BcryptPasswordEncoder();
        return new UserService(userRepo, validator, passwordEncoder);
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
    public void testUserSignupForInvalidUserEmail() {
        String userName = "TestUser";
        String email = "ThisIsNotAnEmail";
        String password = "TestPassword123";
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUserName(userName);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
        UserRepo mockUserRepo = mock(UserRepo.class);
        UserService userService = createUserService(mockUserRepo);
        // Test the signup method with the invalid user
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(signupRequest)
        );
        assertEquals(400, exception.getStatusCode().value());
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

        User existingUser = new User(
                userName,
                "existing@example.com",
                "encoded-password"
        );

        UserRepo mockUserRepo = mock(UserRepo.class);

        when(mockUserRepo.findFirstByUserName(userName))
                .thenReturn(Optional.of(existingUser));

        UserService userService = createUserService(mockUserRepo);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(signupRequest)
        );

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("Username already exists", exception.getReason());

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

        User existingUser = new User(
                "AnotherUser",
                email,
                "encoded-password"
        );

        UserRepo mockUserRepo = mock(UserRepo.class);

        when(mockUserRepo.findFirstByUserName(username))
                .thenReturn(Optional.empty());

        when(mockUserRepo.findFirstByEmail(email))
                .thenReturn(Optional.of(existingUser));

        UserService userService = createUserService(mockUserRepo);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(request)
        );

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("Email already exists", exception.getReason());

        verify(mockUserRepo, never()).save(any(User.class));
    }
    @Test
    public void testUserSignupWithInvalidPassword() {
        SignupRequest request = new SignupRequest();
        request.setUserName("TestUser");
        request.setEmail("test@example.com");
        request.setPassword("weak");

        UserRepo mockUserRepo = mock(UserRepo.class);
        UserService userService = createUserService(mockUserRepo);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(request)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(mockUserRepo, never()).save(any(User.class));
    }
    @Test
    public void testUserSignupInvalidWithBlacnkUserNameEmailAndPassword() {
        SignupRequest request = new SignupRequest();
        request.setUserName("");
        request.setEmail("");
        request.setPassword("");

        UserRepo mockUserRepo = mock(UserRepo.class);
        UserService userService = createUserService(mockUserRepo);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(request)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(mockUserRepo, never()).save(any(User.class));
    }
    @Test
    public void testUserSignUpisInvalidForMaximumLengthOfUserNameEmailAndPassword() {
        SignupRequest request = new SignupRequest();
        request.setUserName("a".repeat(101));
        request.setEmail("a".repeat(101) + "@example.com");
        request.setPassword("A1" + "a".repeat(70));

        UserRepo mockUserRepo = mock(UserRepo.class);
        UserService userService = createUserService(mockUserRepo);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(request)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(mockUserRepo, never()).save(any(User.class));
    }
}
