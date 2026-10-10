package minicode.com.EntityTest;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class SubmissionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savesAValidSubmissionEntity() {
        // Given a persisted user
        User user = new User();
        user.setUserName("testUser");
        user.setEmail("mail@mail.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        User persistedUser = entityManager.persistAndFlush(user);

        // And a persisted challenge
        Challenge challenge = new Challenge(
                "Add Two Numbers",
                "Given two numbers, return their sum.",
                Difficulty.EASY,
                Category.GENERAL,
                2,
                "solution",
                "runner",
                "runnerExplanation",
                "template",
                "templateExplanation",
                "python"
        );
        Challenge persistedChallenge = entityManager.persistAndFlush(challenge);

        // And a valid submission
        String code = "def add(a, b): return a + b";
        String language = "python";
        int exitCode = 0;
        int testCasesPassed = 2;
        int testCasesFailed = 0;
        Submission submission = new Submission(
                persistedUser,
                persistedChallenge,
                code,
                language,
                exitCode,
                SubmissionStatus.ACCEPTED,
                testCasesPassed,
                testCasesFailed
        );

        // When it is saved
        Submission saved = entityManager.persistAndFlush(submission);

        entityManager.clear(); // read back from H2, not the in-memory cache

        // Then it can be read back with the same properties
        Submission submission1 = entityManager.find(Submission.class, saved.getId());
        assertThat(submission1.getId()).isNotNull();                       // generated UUID
        assertThat(submission1.getUser().getId()).isEqualTo(persistedUser.getUserId());
        assertThat(submission1.getChallenge().getId()).isEqualTo(persistedChallenge.getId());
        assertThat(submission1.getCode()).isEqualTo("def add(a, b): return a + b");
        assertThat(submission1.getLanguage()).isEqualTo("python");
        assertThat(submission1.getExitCode()).isZero();
        assertThat(submission1.getStatus()).isEqualTo(SubmissionStatus.ACCEPTED);
        assertThat(submission1.getTestCasesPassed()).isEqualTo(2);
        assertThat(submission1.getTestCasesFailed()).isZero();
        assertThat(submission1.getCreatedAt()).isNotNull();               // @CreationTimestamp
    }
}

