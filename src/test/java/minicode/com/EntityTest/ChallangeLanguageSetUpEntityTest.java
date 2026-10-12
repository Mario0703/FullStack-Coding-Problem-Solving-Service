package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.ChallengeLanguageSetup;
import minicode.com.entity.Challange.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ChallangeLanguageSetUpEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private Challenge persistedChallenge;
    private ChallengeLanguageSetup challengeLanguageSetup;

    @BeforeEach
    public void setup() {
        // Given a persisted challenge
        Challenge challenge = new Challenge();
        challenge.setName("Sample Challenge");
        challenge.setDescription("This is a sample challenge description.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(5);
        this.persistedChallenge = entityManager.persistAndFlush(challenge);

        // And a per-language setup attached to it
        this.challengeLanguageSetup = new ChallengeLanguageSetup();
        challengeLanguageSetup.setChallenge(persistedChallenge);
        challengeLanguageSetup.setLanguage("python");
        challengeLanguageSetup.setTemplate("def solution():\n    pass");
        challengeLanguageSetup.setTemplateExplanation("Fill in the function body.");
        challengeLanguageSetup.setRunner("print(solution())");
        challengeLanguageSetup.setRunnerExplanation("Runs the submitted solution against the test cases.");
    }

    @Test
    public void savingChallengeLanguageSetupWithNullChallengeThrows() {
        // Given the required challenge (foreign key) is null
        challengeLanguageSetup.setChallenge(null);

        // When it is saved and flushed to the database
        // Then the null foreign key is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(challengeLanguageSetup));
    }

    @Test
    public void savingChallengeLanguageSetupWithNullLanguageThrows() {
        // Given the required language is null
        challengeLanguageSetup.setLanguage(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(challengeLanguageSetup));
    }

    @Test
    public void savingChallengeLanguageSetupWithNullTemplateSucceeds() {
        // Given the nullable template is null
        challengeLanguageSetup.setTemplate(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

    @Test
    public void savingChallengeLanguageSetupWithNullTemplateExplanationSucceeds() {
        // Given the nullable template explanation is null
        challengeLanguageSetup.setTemplateExplanation(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

    @Test
    public void savingChallengeLanguageSetupWithNullRunnerSucceeds() {
        // Given the nullable runner is null
        challengeLanguageSetup.setRunner(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

    @Test
    public void savingChallengeLanguageSetupWithNullRunnerExplanationSucceeds() {
        // Given the nullable runner explanation is null
        challengeLanguageSetup.setRunnerExplanation(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

}
