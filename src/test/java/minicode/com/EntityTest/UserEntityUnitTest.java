package minicode.com.EntityTest;

import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.UUID;

public class UserEntityUnitTest {

    @Test
    public void testUserCreationForAValidUser(){
        // Create a new user
        String username = "testUser";
        String email = "mail@mail.com";
        String password = "password";
        int score = 0;
        int numberOfEasyChallengesSolved = 0;
        int numberOfMediumChallengesSolved = 0;
        int numberOfHardChallengesSolved = 0;
        long tokenUsage = 0;
        UserRole role = UserRole.USER;
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        User user = new User();
        // Set the user properties
        user.setUserId(userId);
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setScore(score);
        user.setNumberOfEasyChallengesSolved(numberOfEasyChallengesSolved);
        user.setNumberOfMediumChallengesSolved(numberOfMediumChallengesSolved);
        user.setNumberOfHardChallengesSolved(numberOfHardChallengesSolved);
        user.setTokenUsage(tokenUsage);
        user.setRole(role);

        // Mock the user repository
        UserRepo mockUserRepo = Mockito.mock(UserRepo.class);
        Mockito.when(mockUserRepo.save(user)).thenReturn(user);
        Mockito.when(mockUserRepo.findById(userId)).thenReturn(java.util.Optional.of(user));
    }
    @Test
    public void testUserCreationForAnInvalidUser(){
        // Create a new user with invalid email
        String username = "testUser";
        String email = "invalidEmail"; // Invalid email format
        String password = "password";
        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);

        // Mock the user repository
        UserRepo mockUserRepo = Mockito.mock(UserRepo.class);
        Mockito.when(mockUserRepo.save(user)).thenThrow(new IllegalArgumentException("Invalid email format"));
    }

    @Test
    public void testUserCannotBeSavedWithNullUsername() {
        // Create a new user with null username
        String email = "testEmail@mail.com";
        String password = "password";
        User user = new User();
        user.setUserName(null);
        user.setEmail(email);
        user.setPassword(password);

        // Mock the user repository
        UserRepo mockUserRepo = Mockito.mock(UserRepo.class);
        Mockito.when(mockUserRepo.save(user)).thenThrow(new IllegalArgumentException("Username cannot be null"));
    }
}