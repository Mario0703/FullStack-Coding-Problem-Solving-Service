package minicode.com.EntityTest;

import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.ChallengeLanguageSetup;
import minicode.com.entity.Challange.Difficulty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ChallangeLanguageSetUpEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testChallengeLanguageSetUpEntity() {
        // Given a persisted challenge
        Challenge challenge = new Challenge();
        challenge.setName("Sample Challenge");
        challenge.setCategory(Category.GENERAL);
        challenge.setDescription("This is a sample challenge description.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setLanguage("python");
        challenge.setNumberOfTestCases(5);
        challenge.setSolution("def solution():\n    pass");
        challenge.setRunner("def runner():\n    pass");
        Challenge persistedChallenge = entityManager.persistAndFlush(challenge);

        // And a per-language setup attached to it
        String language = "python";
        String template = "def solution():\n    pass";
        String templateExplanation = "Fill in the function body.";
        String runner = "print(solution())";
        String runnerExplanation = "Runs the submitted solution against the test cases.";

        ChallengeLanguageSetup challengeLanguageSetup = new ChallengeLanguageSetup();
        challengeLanguageSetup.setChallenge(persistedChallenge);
        challengeLanguageSetup.setLanguage(language);
        challengeLanguageSetup.setTemplate(template);
        challengeLanguageSetup.setTemplateExplanation(templateExplanation);
        challengeLanguageSetup.setRunner(runner);
        challengeLanguageSetup.setRunnerExplanation(runnerExplanation);

        // When it is saved and flushed to the database
        ChallengeLanguageSetup saved = entityManager.persistAndFlush(challengeLanguageSetup);
        entityManager.clear(); // read the row back from the database, not the cache

        // Then it can be read back by its assigned primary key with the same properties
        ChallengeLanguageSetup reloaded = entityManager.find(ChallengeLanguageSetup.class, saved.getId());
        assertThat(reloaded).isNotNull();
        assertThat(reloaded.getId()).isEqualTo(saved.getId());
        assertThat(reloaded.getLanguage()).isEqualTo(language);
        assertThat(reloaded.getTemplate()).isEqualTo(template);
        assertThat(reloaded.getTemplateExplanation()).isEqualTo(templateExplanation);
        assertThat(reloaded.getRunner()).isEqualTo(runner);
        assertThat(reloaded.getRunnerExplanation()).isEqualTo(runnerExplanation);
        assertThat(reloaded.getChallenge().getId()).isEqualTo(persistedChallenge.getId());
    }
}
