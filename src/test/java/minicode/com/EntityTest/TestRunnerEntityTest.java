package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.TestRunner.TestRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class TestRunnerEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingTestRunnerWithNullLanguageThrows() {
        // Given a test runner whose required language (the identifier) is null
        TestRunner testRunner = new TestRunner();
        testRunner.setLanguage(null);
        testRunner.setTestRunnerMetaData("Python Test Runner Metadata");

        // When it is saved and flushed to the database
        // Then the null identifier is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(testRunner));
    }

    @Test
    public void savingTestRunnerWithNullTestRunnerMetaDataSucceeds() {
        // Given a valid test runner whose nullable metadata is null
        TestRunner testRunner = new TestRunner();
        testRunner.setLanguage("python");
        testRunner.setTestRunnerMetaData(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(testRunner);
        });
    }

}
