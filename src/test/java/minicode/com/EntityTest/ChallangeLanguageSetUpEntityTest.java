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

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ChallangeLanguageSetUpEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingChallengeLanguageSetupWithNullLanguageThrows() {
        // Given a persisted challenge
        Challenge challenge = new Challenge();
        challenge.setName("Sample Challenge");
        challenge.setDescription("This is a sample challenge description.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(5);
        Challenge persistedChallenge = entityManager.persistAndFlush(challenge);

        // And a per-language setup whose required language is null
        ChallengeLanguageSetup challengeLanguageSetup = new ChallengeLanguageSetup();
        challengeLanguageSetup.setChallenge(persistedChallenge);
        challengeLanguageSetup.setLanguage(null);
        challengeLanguageSetup.setTemplate("def solution():\n    pass");

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(challengeLanguageSetup));
    }
}
