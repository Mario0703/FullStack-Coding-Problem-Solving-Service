package minicode.com.JpaRepo;

import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.repository.ChallengeRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaTestChallengeRepo {

    @Autowired
    private ChallengeRepo challengeRepo;

    @Test
    public void findByIdReturnsChallengeWhenSaved() {
        Challenge challenge = new Challenge();
        challenge.setName("Two Sum");
        challenge.setDescription("Find two numbers that add up to target");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.ARRAYS);
        challenge.setNumberOfTestCases(3);
        Challenge savedChallenge = challengeRepo.save(challenge);

        Optional<Challenge> foundChallenge = challengeRepo.findById(savedChallenge.getId());

        assertThat(foundChallenge.isPresent()).isTrue();
        assertThat(foundChallenge.get().getName()).isEqualTo("Two Sum");
        assertThat(foundChallenge.get().getDifficulty()).isEqualTo(Difficulty.EASY);
        assertThat(foundChallenge.get().getCategory()).isEqualTo(Category.ARRAYS);
    }

}
