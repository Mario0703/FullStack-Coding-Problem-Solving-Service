package minicode.com.EntityTest;

import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ChallengeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testChallengeEntity() {
        // Given a challenge with all of its properties
        String name = "Two Sum";
        String description = "Return the indices of the two numbers that add up to the target.";
        Difficulty difficulty = Difficulty.EASY;
        Category category = Category.ARRAYS;
        int numberOfTestCases = 5;
        String solution = "def two_sum(nums, target):\n    pass";
        String runner = "print(two_sum([2, 7, 11, 15], 9))";
        String runnerExplanation = "Runs the submitted solution against the test cases.";
        String templete = "def two_sum(nums, target):\n    # your code here\n    pass";
        String templeteExplanation = "Return the two indices.";
        String hints = "Use a hash map.\nWatch out for duplicate values.";
        String language = "python";

        Challenge challenge = new Challenge();
        challenge.setName(name);
        challenge.setDescription(description);
        challenge.setDifficulty(difficulty);
        challenge.setCategory(category);
        challenge.setNumberOfTestCases(numberOfTestCases);
        challenge.setSolution(solution);
        challenge.setRunner(runner);
        challenge.setRunnerExplanation(runnerExplanation);
        challenge.setTemplete(templete);
        challenge.setTempleteExplanation(templeteExplanation);
        challenge.setHints(hints);
        challenge.setLanguage(language);

        // When it is saved and flushed to the database
        Challenge saved = entityManager.persistAndFlush(challenge);
        entityManager.clear(); // read the row back from the database, not the cache

        // Then it can be read back with the same properties
        Challenge retrievedChallenge = entityManager.find(Challenge.class, saved.getId());
        assertThat(retrievedChallenge).isNotNull();
        assertThat(retrievedChallenge.getId()).isEqualTo(saved.getId());
        assertThat(retrievedChallenge.getName()).isEqualTo(name);
        assertThat(retrievedChallenge.getDescription()).isEqualTo(description);
        assertThat(retrievedChallenge.getDifficulty()).isEqualTo(difficulty);
        assertThat(retrievedChallenge.getCategory()).isEqualTo(category);
        assertThat(retrievedChallenge.getNumberOfTestCases()).isEqualTo(numberOfTestCases);
        assertThat(retrievedChallenge.getSolution()).isEqualTo(solution);
        assertThat(retrievedChallenge.getRunner()).isEqualTo(runner);
        assertThat(retrievedChallenge.getRunnerExplanation()).isEqualTo(runnerExplanation);
        assertThat(retrievedChallenge.getTemplete()).isEqualTo(templete);
        assertThat(retrievedChallenge.getTempleteExplanation()).isEqualTo(templeteExplanation);
        assertThat(retrievedChallenge.getHints()).isEqualTo(hints);
        assertThat(retrievedChallenge.getLanguage()).isEqualTo(language);
    }
}
