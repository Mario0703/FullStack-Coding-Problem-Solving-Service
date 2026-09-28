package minicode.com.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Challenges", schema = "public") // provides the name of the table

public class Challenge {
    protected Challenge() {
    }

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Column(name = "catagory", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "number_of_test_cases", nullable = false, columnDefinition = "integer default 0")
    private int numberOfTestCases;

    public Challenge(
            String name,
            String description,
            Difficulty difficulty,
            Category category,
            int numberOfTestCases
    ) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.numberOfTestCases = numberOfTestCases;
    }
}
