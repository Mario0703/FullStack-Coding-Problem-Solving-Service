package minicode.com.EntityTest;

import minicode.com.entity.Challange.SolvedChallenges;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class SolvedChallengeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSolvedChallengeEntity() {
        SolvedChallenges solvedChallenge = new SolvedChallenges();
        solvedChallenge.setChallengeId(UUID.randomUUID());
        solvedChallenge.setLanguage("python");
        solvedChallenge.setUserId(UUID.randomUUID());
        solvedChallenge.setCode("def add(a, b): return a + b");
        solvedChallenge.setSolvedAt(Instant.now());
        SolvedChallenges persistedSolvedChallenge = entityManager.persistAndFlush(solvedChallenge);
        entityManager.clear();
        SolvedChallenges foundSolvedChallenge = entityManager.find(SolvedChallenges.class, persistedSolvedChallenge.getSolvedChallengeId());
        assertThat(foundSolvedChallenge.getChallengeId()).isEqualTo(solvedChallenge.getChallengeId());
        assertThat(foundSolvedChallenge.getLanguage()).isEqualTo(solvedChallenge.getLanguage());
        assertThat(foundSolvedChallenge.getUserId()).isEqualTo(solvedChallenge.getUserId());
        assertThat(foundSolvedChallenge.getCode()).isEqualTo(solvedChallenge.getCode());
        assertThat(foundSolvedChallenge.getSolvedAt()).isEqualTo(solvedChallenge.getSolvedAt());
        assertThat(foundSolvedChallenge.getSolvedChallengeId()).isEqualTo(solvedChallenge.getSolvedChallengeId());





    }
}
