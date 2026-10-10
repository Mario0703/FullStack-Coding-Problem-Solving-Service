package minicode.com.EntityTest;

import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserEntityTest {

    private User user;


    @BeforeEach
    private void setUp() {
        user = new User();
    }



    @Test
    public void testUserCreationForAValidUser(){
        // Given a user with valid properties
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
        // when the user is created with the given properties
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

        // Then the user properties should be set correctly
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getUserName()).isEqualTo(username);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getScore()).isEqualTo(score);
        assertThat(user.getNumberOfEasyChallengesSolved()).isEqualTo(numberOfEasyChallengesSolved);
        assertThat(user.getNumberOfMediumChallengesSolved()).isEqualTo(numberOfMediumChallengesSolved);
        assertThat(user.getNumberOfHardChallengesSolved()).isEqualTo(numberOfHardChallengesSolved);
        assertThat(user.getTokenUsage()).isEqualTo(tokenUsage);
        assertThat(user.getRole()).isEqualTo(role);
    }

}