package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Challange.TestCases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class TestCasesEntityTest {

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void savingTestCasesWithNullChallengeThrows() {
        // Given a test case whose required challenge is null
        TestCases testCases = new TestCases();
        testCases.setTestInput("1 2");
        testCases.setTestOutput("3");
        testCases.setChallenge(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(testCases));
    }

    @Test
    public void savingTestCasesWithNullTestInputSucceeds() {
        // Given a valid test case whose nullable input is null
        TestCases testCases = newValidTestCases();
        testCases.setTestInput(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(testCases);
        });
    }

    @Test
    public void savingTestCasesWithNullTestOutputSucceeds() {
        // Given a valid test case whose nullable output is null
        TestCases testCases = newValidTestCases();
        testCases.setTestOutput(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(testCases);
        });
    }

    private TestCases newValidTestCases() {
        TestCases testCases = new TestCases();
        testCases.setTestInput("1 2");
        testCases.setTestOutput("3");
        testCases.setChallenge(newPersistedChallenge());
        return testCases;
    }

    private Challenge newPersistedChallenge() {
        Challenge challenge = new Challenge();
        challenge.setName("Test Challenge");
        challenge.setDescription("A random test challenge");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(2);
        return entityManager.persistAndFlush(challenge);
    }

}
