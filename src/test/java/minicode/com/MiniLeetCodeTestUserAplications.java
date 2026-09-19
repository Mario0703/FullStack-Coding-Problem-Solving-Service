package minicode.com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MiniLeetCodeTestUserAplications {

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
        MiniLeetCodeApplication application = new MiniLeetCodeApplication(mockUserRepo);
        User savedUser = application.signup(signupRequest);
        // Verify that the user was saved correctly
        assert savedUser.getUserName().equals(username);
        assert savedUser.getEmail().equals(email);
        assert passwordEncoder.matches(password, savedUser.getPassword());
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
        MiniLeetCodeApplication application = new MiniLeetCodeApplication(mockUserRepo);
        // Test the signup method with the invalid user
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> application.signup(signupRequest)
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

        MiniLeetCodeApplication application =
                new MiniLeetCodeApplication(mockUserRepo);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> application.signup(signupRequest)
        );

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("Username already exists", exception.getReason());

        verify(mockUserRepo, never()).save(any(User.class));
    }
}
