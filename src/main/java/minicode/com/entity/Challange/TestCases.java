package minicode.com.entity.Challange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "test_cases", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCases {
    @Column
    private String testInput;
    @Column
    private String testOutput;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    @JsonIgnore
    private Challenge challenge;
}




