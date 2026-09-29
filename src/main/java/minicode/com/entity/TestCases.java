package minicode.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "test_cases", schema = "public") // provides the name of the table
@Getter
@Setter
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
