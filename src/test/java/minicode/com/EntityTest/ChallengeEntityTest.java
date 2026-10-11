package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ChallengeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingChallengeWithNullNameThrows() {
        // Given a challenge whose required name is null
        Challenge challenge = new Challenge();
        challenge.setName(null);
        challenge.setDescription("Return the indices of the two numbers that add up to the target.");
        challenge.setDifficulty(Difficulty.EASY);
        challenge.setCategory(Category.ARRAYS);
        challenge.setNumberOfTestCases(5);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(challenge));
    }
}
