package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.entity.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class SubmissionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private User persistedUser;
    private Challenge persistedChallenge;
    private Submission submission;

    @BeforeEach
    public void setup(){
        // Given a persisted user
        User user = new User();
        user.setUserName("testUser");
        user.setEmail("mail@mail.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        this.persistedUser = entityManager.persistAndFlush(user);

        // And a persisted challenge
        Challenge challenge = new Challenge();
        challenge.setName("Add Two Numbers");
        challenge.setDescription("Given two numbers, return their sum.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(2);
        this.persistedChallenge = entityManager.persistAndFlush(challenge);

        // And a submission that maps to that user and challenge
        this.submission = new Submission();
        submission.setUser(persistedUser);
        submission.setChallenge(persistedChallenge);
        submission.setCode("print('Hello, World!')");
        submission.setLanguage("python");
        submission.setExitCode(0);
        submission.setStatus(SubmissionStatus.ACCEPTED);
    }

    @Test
    public void savingSubmissionWithNullCodeThrows() {
        // Given the submission has no code
        submission.setCode(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(submission));
    }

    @Test
    public void SavingWithNullLanguageThrows() {
        // Given the submission has no language
        submission.setLanguage(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(submission));
    }

    @Test
    public void SavingWithNullSetStatusThrows() {
        // Given the submission has no status
        submission.setStatus(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(submission));
    }

    @Test
    public void submissionMapsToTheSameChallengeWhenSubmitted() {
        // When it is saved and flushed to the database
        entityManager.persistAndFlush(submission);

        // Then the reloaded submission points at the same challenge (from the foreign key)
        entityManager.clear();
        Submission reloaded = entityManager.find(Submission.class, submission.getId());

        assertThat(reloaded.getChallenge().getId()).isEqualTo(persistedChallenge.getId());
    }

    @Test
    public void submissionMapsToTheSameUserWhenSubmitted() {
        // When it is saved and flushed to the database
        entityManager.persistAndFlush(submission);

        // Then the reloaded submission points at the same user (from the foreign key)
        entityManager.clear();
        Submission reloaded = entityManager.find(Submission.class, submission.getId());

        assertThat(reloaded.getUser().getUserId()).isEqualTo(persistedUser.getUserId());
    }

}
