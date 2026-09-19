package minicode.com;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MiniLeetCodeTestUserAplications {

    @Test
    public void testUserSignupForAValidUser() {
        String username = "TestUser";
        String email = "testuser@gmail.com";
        String password = "TestPassword123";
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
                saveUser ->{
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

    }
}
