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
    private Submission persistedSubmission;

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

        // And a persisted submission for that user and challenge
        Submission submission = new Submission();
        submission.setUser(persistedUser);
        submission.setChallenge(persistedChallenge);
        submission.setCode("print('Hello, World!')");
        submission.setLanguage("python");
        submission.setExitCode(0);
        submission.setStatus(SubmissionStatus.ACCEPTED);
        this.persistedSubmission = entityManager.persistAndFlush(submission);
    }

    @Test
    public void savingSubmissionWithNullCodeThrows() {
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

    @Test
    public void SavingWithNullLanguageThrows() {
        // And a submission whose required language is null
        Submission submission = new Submission();
        submission.setUser(persistedUser);
        submission.setChallenge(persistedChallenge);
        submission.setCode("print('Hello, World!')");
        submission.setLanguage(null);
        submission.setExitCode(0);
        submission.setStatus(SubmissionStatus.ACCEPTED);
        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(submission));
    }

    @Test
    public void SavingWithNullSetStatusThrows() {
        // And a submission whose required status is null
        Submission submission = new Submission();
        submission.setUser(persistedUser);
        submission.setChallenge(persistedChallenge);
        submission.setCode("print('Hello, World!')");
        submission.setLanguage("python");
        submission.setExitCode(0);
        submission.setStatus(null);
        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(submission));
    }

    @Test
    public void submissionMapsToTheSameChallengeWhenSubmitted() {
        // Reload from the database so the challenge relation comes from the foreign key
        entityManager.clear();
        Submission reloaded = entityManager.find(Submission.class, persistedSubmission.getId());

        assertThat(reloaded.getChallenge().getId()).isEqualTo(persistedChallenge.getId());
    }

    @Test
    public void submissionMapsToTheSameUserWhenSubmitted() {
        // Reload from the database so the user relation comes from the foreign key
        entityManager.clear();
        Submission reloaded = entityManager.find(Submission.class, persistedSubmission.getId());

        assertThat(reloaded.getUser().getUserId()).isEqualTo(persistedUser.getUserId());
    }

}
