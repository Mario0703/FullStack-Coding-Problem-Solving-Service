package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.SolvedChallenges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class SolvedChallengeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private SolvedChallenges solvedChallenge;

    @BeforeEach
    public void setup() {
        // Given a solved challenge with a user, challenge, code, language and timestamp
        this.solvedChallenge = new SolvedChallenges();
        solvedChallenge.setUserId(UUID.randomUUID());
        solvedChallenge.setChallengeId(UUID.randomUUID());
        solvedChallenge.setCode("def add(a, b): return a + b");
        solvedChallenge.setLanguage("python");
        solvedChallenge.setSolvedAt(Instant.now());
    }

    @Test
    public void savingSolvedChallengeWithNullSolvedAtThrows() {
        // Given the required solved-at timestamp is null
        solvedChallenge.setSolvedAt(null);

        // When it is saved and flushed to the database
        // Then the null required timestamp is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(solvedChallenge));
    }

    @Test
    public void savingSolvedChallengeWithNullUserIdSucceeds() {
        // Given the nullable user id is null
        solvedChallenge.setUserId(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

    @Test
    public void savingSolvedChallengeWithNullChallengeIdSucceeds() {
        // Given the nullable challenge id is null
        solvedChallenge.setChallengeId(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

    @Test
    public void savingSolvedChallengeWithNullCodeSucceeds() {
        // Given the nullable code is null
        solvedChallenge.setCode(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

    @Test
    public void savingSolvedChallengeWithNullLanguageSucceeds() {
        // Given the nullable language is null
        solvedChallenge.setLanguage(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

}
