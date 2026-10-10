package minicode.com.EntityTest;

import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testUserCreationForAValidUser(){
        // Given a user with valid properties
        User user = new User();
        String username = "testUser";
        String email = "mail@mail.com";
        String password = "password";
        int score = 0;
        int numberOfEasyChallengesSolved = 0;
        int numberOfMediumChallengesSolved = 0;
        int numberOfHardChallengesSolved = 0;
        long tokenUsage = 0;
        UserRole role = UserRole.USER;
        // when the user is created with the given properties
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setScore(score);
        user.setNumberOfEasyChallengesSolved(numberOfEasyChallengesSolved);
        user.setNumberOfMediumChallengesSolved(numberOfMediumChallengesSolved);
        user.setNumberOfHardChallengesSolved(numberOfHardChallengesSolved);
        user.setTokenUsage(tokenUsage);
        user.setRole(role);

        // And it is saved and flushed to the database
        User saved = entityManager.persistAndFlush(user);
        entityManager.clear(); // read the row back from the database, not the cache

        // Then it can be read back with the same properties
        User retrievedUser = entityManager.find(User.class, saved.getUserId());
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUserId()).isEqualTo(saved.getUserId());
        assertThat(retrievedUser.getUserName()).isEqualTo(username);
        assertThat(retrievedUser.getEmail()).isEqualTo(email);
        assertThat(retrievedUser.getPassword()).isEqualTo(password);
        assertThat(retrievedUser.getScore()).isEqualTo(score);
        assertThat(retrievedUser.getNumberOfEasyChallengesSolved()).isEqualTo(numberOfEasyChallengesSolved);
        assertThat(retrievedUser.getNumberOfMediumChallengesSolved()).isEqualTo(numberOfMediumChallengesSolved);
        assertThat(retrievedUser.getNumberOfHardChallengesSolved()).isEqualTo(numberOfHardChallengesSolved);
        assertThat(retrievedUser.getTokenUsage()).isEqualTo(tokenUsage);
        assertThat(retrievedUser.getRole()).isEqualTo(role);
    }

}
