package minicode.com.repository;

import minicode.com.entity.TestRunner.TestRunner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRunnerRepo extends JpaRepository<TestRunner, String> {
}
