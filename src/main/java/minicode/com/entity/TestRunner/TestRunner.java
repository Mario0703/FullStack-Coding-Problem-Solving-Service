package minicode.com.entity.TestRunner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "test_runners", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestRunner {

    @Id
    @Column(nullable = false, length = 20)
    private String language;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String testRunnerMetaData;
}
