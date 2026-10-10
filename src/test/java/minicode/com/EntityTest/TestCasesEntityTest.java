package minicode.com.EntityTest;

import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Challange.TestCases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TestCasesEntityTest {

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testTestCasesEntity(){
        // Given a random challenge to attach the test case to
        Challenge challenge = new Challenge();
        challenge.setName("Test Challenge " + UUID.randomUUID());
        challenge.setDescription("A random test challenge");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(2);
        challenge.setSolution("solution");
        challenge.setRunner("runner");
        challenge.setRunnerExplanation("runner explanation");
        challenge.setTemplete("template");
        challenge.setTempleteExplanation("template explanation");
        challenge.setLanguage("python");
        entityManager.persistAndFlush(challenge);
        entityManager.clear();
        // And a test case with an input and expected output
        String testInput = "1 2";
        String testOutput = "3";
        TestCases testCases = new TestCases();
        testCases.setTestInput(testInput);
        testCases.setTestOutput(testOutput);
        testCases.setChallenge(challenge);

        // When it is saved and flushed to the database
        TestCases saved = entityManager.persistAndFlush(testCases);
        entityManager.clear(); // read the row back from the database, not the cache

        // Then it can be read back with the same properties
        TestCases reloaded = entityManager.find(TestCases.class, saved.getId());
        assertThat(reloaded).isNotNull();
        assertThat(reloaded.getId()).isEqualTo(saved.getId());
        assertThat(reloaded.getTestInput()).isEqualTo(testInput);
        assertThat(reloaded.getTestOutput()).isEqualTo(testOutput);
        assertThat(reloaded.getChallenge().getId()).isEqualTo(challenge.getId());
    }

}
