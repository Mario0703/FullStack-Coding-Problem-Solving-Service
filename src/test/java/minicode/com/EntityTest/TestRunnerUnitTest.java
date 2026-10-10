package minicode.com.EntityTest;

import minicode.com.entity.TestRunner.TestRunner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TestRunnerUnitTest {

    private TestRunner testRunner;


    @Test
    public void testValidTestRunnerForPython(){
        // Given a test runner with valid properties
        String language = "python";
        String MetaData = "Python Test Runner Metadata";
        // when the test runner is created with the given properties
        testRunner = new TestRunner(language, MetaData);
        // Then the test runner properties should be set correctly
        assertThat(testRunner.getLanguage()).isEqualTo(language);
        assertThat(testRunner.getTestRunnerMetaData()).isEqualTo(MetaData);

    }


}
