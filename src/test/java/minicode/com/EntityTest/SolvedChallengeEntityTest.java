package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.SolvedChallenges;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class SolvedChallengeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingSolvedChallengeWithNullSolvedAtThrows() {
        // Given a solved challenge whose required solved-at timestamp is null
        SolvedChallenges solvedChallenge = new SolvedChallenges();
        solvedChallenge.setUserId(UUID.randomUUID());
        solvedChallenge.setChallengeId(UUID.randomUUID());
        solvedChallenge.setCode("def add(a, b): return a + b");
        solvedChallenge.setLanguage("python");
        solvedChallenge.setSolvedAt(null);

        // When it is saved and flushed to the database
        // Then the null required timestamp is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(solvedChallenge));
    }
}
