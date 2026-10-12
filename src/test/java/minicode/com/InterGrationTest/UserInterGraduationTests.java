package minicode.com.InterGrationTest;

import minicode.com.dto.User.Request.SignupRequest;
import minicode.com.exception.SignupException;
import minicode.com.repository.UserRepo;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserInterGraduationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void whenTwoUsersShareAUserNameTheSecondSignupIsRejected() {

        SignupRequest firstUser = signUpRequest("sharedName", "user1@example.com");
        SignupRequest secondUser = signUpRequest("sharedName", "user2@example.com");

        userService.signup(firstUser);
        userRepo.flush();

        SignupException exception = assertThrows(
                SignupException.class,
                () -> userService.signup(secondUser)
        );

        assertEquals("Username already exists: sharedName", exception.getMessage());
    }

    @Test
    public void whenTwoUsersShareAnEmailTheSecondSignupIsRejected() {
        SignupRequest firstUser = signUpRequest("sharedName1", "user1@example.com");
        SignupRequest secondUser = signUpRequest("sharedName2", "user1@example.com");
        userService.signup(firstUser);
        userRepo.flush();

        SignupException exception = assertThrows(
                SignupException.class,
                () -> userService.signup(secondUser)
        );
        assertEquals("Email already exists: user1@example.com", exception.getMessage());
    }
    private SignupRequest signUpRequest(String userName, String email) {
        SignupRequest request = new SignupRequest();
        request.setUserName(userName);
        request.setEmail(email);
        request.setPassword("Password123");
        return request;
    }
}
