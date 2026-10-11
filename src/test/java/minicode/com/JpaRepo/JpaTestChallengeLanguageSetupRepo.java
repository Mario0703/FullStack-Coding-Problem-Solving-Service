package minicode.com.JpaRepo;

import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.ChallengeLanguageSetup;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.repository.ChallengeLanguageSetupRepo;
import minicode.com.repository.ChallengeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaTestChallengeLanguageSetupRepo {

    @Autowired
    private ChallengeLanguageSetupRepo challengeLanguageSetupRepo;

    @Autowired
    private ChallengeRepo challengeRepo;

    private Challenge challenge;

    @BeforeEach
    public void setUp(){

        this.challenge = new Challenge();
        challenge.setName("Two Sum");
        challenge.setDescription("Find two numbers that add up to target");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.ARRAYS);
        challenge.setNumberOfTestCases(3);
        challengeRepo.saveAndFlush(challenge);
    }

    @Test
    public void findByIdReturnsChallengeLanguageSetupWhenSaved() {
        ChallengeLanguageSetup setup = new ChallengeLanguageSetup();
        setup.setChallenge(challenge);
        setup.setLanguage("python");
        setup.setTemplate("def two_sum(nums, target):\n    pass");
        setup.setTemplateExplanation("Starter template");
        setup.setRunner("# runner code");
        ChallengeLanguageSetup savedSetup = challengeLanguageSetupRepo.save(setup);

        Optional<ChallengeLanguageSetup> foundSetup = challengeLanguageSetupRepo.findById(savedSetup.getId());

        assertThat(foundSetup.isPresent()).isTrue();
        assertThat(foundSetup.get().getLanguage()).isEqualTo("python");
        assertThat(foundSetup.get().getTemplate()).isEqualTo("def two_sum(nums, target):\n    pass");
    }

    @Test
    public void findByChallengeIdAndLanguageReturnsSetup() {
        UUID challengeId = challenge.getId();

        ChallengeLanguageSetup pythonSetup = new ChallengeLanguageSetup();
        pythonSetup.setChallenge(challenge);
        pythonSetup.setLanguage("python");
        pythonSetup.setTemplate("def two_sum(nums, target):\n    pass");
        challengeLanguageSetupRepo.save(pythonSetup);

        ChallengeLanguageSetup javaSetup = new ChallengeLanguageSetup();
        javaSetup.setChallenge(challenge);
        javaSetup.setLanguage("java");
        javaSetup.setTemplate("class Solution { public int[] twoSum(int[] nums, int target) { } }");
        challengeLanguageSetupRepo.save(javaSetup);

        Optional<ChallengeLanguageSetup> foundPython = challengeLanguageSetupRepo
                .findByChallenge_IdAndLanguage(challengeId, "python");
        Optional<ChallengeLanguageSetup> foundJava = challengeLanguageSetupRepo
                .findByChallenge_IdAndLanguage(challengeId, "java");
        Optional<ChallengeLanguageSetup> foundCsharp = challengeLanguageSetupRepo
                .findByChallenge_IdAndLanguage(challengeId, "csharp");

        assertThat(foundPython.isPresent()).isTrue();
        assertThat(foundPython.get().getLanguage()).isEqualTo("python");

        assertThat(foundJava.isPresent()).isTrue();
        assertThat(foundJava.get().getLanguage()).isEqualTo("java");

        assertThat(foundCsharp.isEmpty()).isTrue();
    }


}
