package minicode.com.EntityTest;

import minicode.com.entity.TestRunner.TestRunner;
import minicode.com.entity.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TestRunnerUnitTest {

    @Autowired
    private TestEntityManager entityManager;



    @Test
    public void testValidTestRunnerForPython(){
        // Given a test runner with valid properties
        String language = "python";
        String MetaData = "Python Test Runner Metadata";
        // when the test runner is created with the given properties
        TestRunner testrunner = entityManager.persistAndFlush(new TestRunner(language, MetaData));
        entityManager.clear();
        // Then the test runner properties should be set correctly
        TestRunner retrievedTestRunner = entityManager.find(TestRunner.class, testrunner.getLanguage());

        assertThat(retrievedTestRunner.getLanguage()).isEqualTo(language);
        assertThat(retrievedTestRunner.getTestRunnerMetaData()).isEqualTo(MetaData);

    }


}
