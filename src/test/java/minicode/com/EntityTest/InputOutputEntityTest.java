package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Challange.InputOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class InputOutputEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private Challenge persistedChallenge;
    private InputOutput inputOutput;

    @BeforeEach
    public void setup() {
        // Given a persisted challenge
        Challenge challenge = new Challenge();
        challenge.setName("Sample Challenge");
        challenge.setDescription("This is a sample challenge description.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(2);
        this.persistedChallenge = entityManager.persistAndFlush(challenge);

        // And an example input/output attached to it
        this.inputOutput = new InputOutput();
        inputOutput.setInputExample("1 2");
        inputOutput.setOutputExample("3");
        inputOutput.setChallenge(persistedChallenge);
    }

    @Test
    public void savingInputOutputWithNullChallengeThrows() {
        // Given the required challenge (foreign key) is null
        inputOutput.setChallenge(null);

        // When it is saved and flushed to the database
        // Then the null foreign key is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(inputOutput));
    }

    @Test
    public void savingInputOutputWithNullInputExampleSucceeds() {
        // Given the nullable input example is null
        inputOutput.setInputExample(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(inputOutput);
        });
    }

    @Test
    public void savingInputOutputWithNullOutputExampleSucceeds() {
        // Given the nullable output example is null
        inputOutput.setOutputExample(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(inputOutput);
        });
    }

}
