package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.ChallengeLanguageSetup;
import minicode.com.entity.Challange.Difficulty;
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

    @Test
    public void savingChallengeLanguageSetupWithNullLanguageThrows() {
        // Given a setup whose required language is null
        ChallengeLanguageSetup challengeLanguageSetup = newValidSetup();
        challengeLanguageSetup.setLanguage(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(challengeLanguageSetup));
    }

    @Test
    public void savingChallengeLanguageSetupWithNullTemplateSucceeds() {
        // Given a valid setup whose nullable template is null
        ChallengeLanguageSetup challengeLanguageSetup = newValidSetup();
        challengeLanguageSetup.setTemplate(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

    @Test
    public void savingChallengeLanguageSetupWithNullTemplateExplanationSucceeds() {
        // Given a valid setup whose nullable template explanation is null
        ChallengeLanguageSetup challengeLanguageSetup = newValidSetup();
        challengeLanguageSetup.setTemplateExplanation(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

    @Test
    public void savingChallengeLanguageSetupWithNullRunnerSucceeds() {
        // Given a valid setup whose nullable runner is null
        ChallengeLanguageSetup challengeLanguageSetup = newValidSetup();
        challengeLanguageSetup.setRunner(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

    @Test
    public void savingChallengeLanguageSetupWithNullRunnerExplanationSucceeds() {
        // Given a valid setup whose nullable runner explanation is null
        ChallengeLanguageSetup challengeLanguageSetup = newValidSetup();
        challengeLanguageSetup.setRunnerExplanation(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challengeLanguageSetup);
        });
    }

    private ChallengeLanguageSetup newValidSetup() {
        ChallengeLanguageSetup challengeLanguageSetup = new ChallengeLanguageSetup();
        challengeLanguageSetup.setChallenge(newPersistedChallenge());
        challengeLanguageSetup.setLanguage("python");
        return challengeLanguageSetup;
    }

    private Challenge newPersistedChallenge() {
        Challenge challenge = new Challenge();
        challenge.setName("Sample Challenge");
        challenge.setDescription("This is a sample challenge description.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(5);
        return entityManager.persistAndFlush(challenge);
    }

}
