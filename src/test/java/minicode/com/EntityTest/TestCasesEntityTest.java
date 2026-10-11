package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Challange.TestCases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class TestCasesEntityTest {

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void savingTestCasesWithNullChallengeThrows() {
        // Given a test case whose required challenge is null
        TestCases testCases = new TestCases();
        testCases.setTestInput("1 2");
        testCases.setTestOutput("3");
        testCases.setChallenge(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(testCases));
    }

}
