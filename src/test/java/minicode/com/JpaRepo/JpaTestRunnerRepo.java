package minicode.com.JpaRepo;

import minicode.com.entity.TestRunner.TestRunner;
import minicode.com.repository.TestRunnerRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaTestRunnerRepo {

    @Autowired
    private TestRunnerRepo testRunnerRepo;

    @BeforeEach
    public void setUp() {
        TestRunner testRunner = new TestRunner();
        testRunner.setLanguage("python");
        testRunner.setTestRunnerMetaData("Python Test Runner Metadata");
        testRunnerRepo.save(testRunner);
    }

    @Test
    public void findTestRunnerByLanguage() {
        TestRunner foundTestRunner = testRunnerRepo.findById("python").orElse(null);
        assertThat(foundTestRunner.getLanguage().equals("python"));
        assertThat(foundTestRunner.getTestRunnerMetaData().equals("Python Test Runner Metadata"));
    }

    @AfterEach
    public void tearDown() {
        testRunnerRepo.deleteAll();
    }
}
