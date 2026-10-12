package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ChallengeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingChallengeWithNullNameThrows() {
        // Given a challenge whose required name is null
        Challenge challenge = newValidChallenge();
        challenge.setName(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(challenge));
    }

    @Test
    public void savingChallengeWithNullSolutionSucceeds() {
        // Given a valid challenge whose nullable solution is null
        Challenge challenge = newValidChallenge();
        challenge.setSolution(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challenge);
        });
    }

    @Test
    public void savingChallengeWithNullRunnerSucceeds() {
        // Given a valid challenge whose nullable runner is null
        Challenge challenge = newValidChallenge();
        challenge.setRunner(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challenge);
        });
    }

    @Test
    public void savingChallengeWithNullRunnerExplanationSucceeds() {
        // Given a valid challenge whose nullable runner explanation is null
        Challenge challenge = newValidChallenge();
        challenge.setRunnerExplanation(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challenge);
        });
    }

    @Test
    public void savingChallengeWithNullTempleteSucceeds() {
        // Given a valid challenge whose nullable template is null
        Challenge challenge = newValidChallenge();
        challenge.setTemplete(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challenge);
        });
    }

    @Test
    public void savingChallengeWithNullTempleteExplanationSucceeds() {
        // Given a valid challenge whose nullable template explanation is null
        Challenge challenge = newValidChallenge();
        challenge.setTempleteExplanation(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challenge);
        });
    }

    @Test
    public void savingChallengeWithNullHintsSucceeds() {
        // Given a valid challenge whose nullable hints are null
        Challenge challenge = newValidChallenge();
        challenge.setHints(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challenge);
        });
    }

    @Test
    public void savingChallengeWithNullLanguageSucceeds() {
        // Given a valid challenge whose nullable language is null
        Challenge challenge = newValidChallenge();
        challenge.setLanguage(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(challenge);
        });
    }

    private Challenge newValidChallenge() {
        Challenge challenge = new Challenge();
        challenge.setName("Two Sum");
        challenge.setDescription("Return the indices of the two numbers that add up to the target.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.ARRAYS);
        return challenge;
    }

}
