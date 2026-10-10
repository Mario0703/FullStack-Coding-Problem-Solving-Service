package minicode.com.entity.TestRunner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "test_runners", schema = "public")
@Getter
@Setter
public class TestRunner {

    @Id
    @Column(nullable = false, length = 20)
    private String language;

    @Column(columnDefinition = "TEXT")
    private String testRunnerMetaData;

    public TestRunner() {
    }

    public TestRunner(String language, String testRunnerMetaData) {
        this.language = language;
        this.testRunnerMetaData = testRunnerMetaData;
    }
}
