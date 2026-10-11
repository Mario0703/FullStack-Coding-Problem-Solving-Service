package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.SolvedChallenges;
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

    @Test
    public void savingSolvedChallengeWithNullSolvedAtThrows() {
        // Given a solved challenge whose required solved-at timestamp is null
        SolvedChallenges solvedChallenge = newValidSolvedChallenge();
        solvedChallenge.setSolvedAt(null);

        // When it is saved and flushed to the database
        // Then the null required timestamp is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(solvedChallenge));
    }

    @Test
    public void savingSolvedChallengeWithNullUserIdSucceeds() {
        // Given a valid solved challenge whose nullable user id is null
        SolvedChallenges solvedChallenge = newValidSolvedChallenge();
        solvedChallenge.setUserId(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

    @Test
    public void savingSolvedChallengeWithNullChallengeIdSucceeds() {
        // Given a valid solved challenge whose nullable challenge id is null
        SolvedChallenges solvedChallenge = newValidSolvedChallenge();
        solvedChallenge.setChallengeId(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

    @Test
    public void savingSolvedChallengeWithNullCodeSucceeds() {
        // Given a valid solved challenge whose nullable code is null
        SolvedChallenges solvedChallenge = newValidSolvedChallenge();
        solvedChallenge.setCode(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

    @Test
    public void savingSolvedChallengeWithNullLanguageSucceeds() {
        // Given a valid solved challenge whose nullable language is null
        SolvedChallenges solvedChallenge = newValidSolvedChallenge();
        solvedChallenge.setLanguage(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(solvedChallenge);
        });
    }

    private SolvedChallenges newValidSolvedChallenge() {
        SolvedChallenges solvedChallenge = new SolvedChallenges();
        solvedChallenge.setUserId(UUID.randomUUID());
        solvedChallenge.setChallengeId(UUID.randomUUID());
        solvedChallenge.setCode("def add(a, b): return a + b");
        solvedChallenge.setLanguage("python");
        solvedChallenge.setSolvedAt(Instant.now());
        return solvedChallenge;
    }

}
