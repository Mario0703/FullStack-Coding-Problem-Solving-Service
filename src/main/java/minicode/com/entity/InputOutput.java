package minicode.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "input_output", schema = "public") // provides the name of the table
@Getter
@Setter
public class InputOutput {
    @Column
    private String inputExample;
    @Column
    private String outputExample;
    @ManyToOne()
    @JoinColumn(name = "challenge_id", nullable = false)
    @JsonIgnore
    private Challenge challenge;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
