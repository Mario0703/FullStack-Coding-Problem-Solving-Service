package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.entity.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class SubmissionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savingSubmissionWithNullCodeThrows() {
        // Given a persisted user
        User user = new User();
        user.setUserName("testUser");
        user.setEmail("mail@mail.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        User persistedUser = entityManager.persistAndFlush(user);

        // And a persisted challenge
        Challenge challenge = new Challenge();
        challenge.setName("Add Two Numbers");
        challenge.setDescription("Given two numbers, return their sum.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(2);
        Challenge persistedChallenge = entityManager.persistAndFlush(challenge);

        // And a submission whose required code is null
        Submission submission = new Submission();
        submission.setUser(persistedUser);
        submission.setChallenge(persistedChallenge);
        submission.setCode(null);
        submission.setLanguage("python");
        submission.setExitCode(0);
        submission.setStatus(SubmissionStatus.ACCEPTED);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(submission));
    }
}
