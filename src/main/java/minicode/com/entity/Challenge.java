package minicode.com.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "challenges", schema = "public") // provides the name of the table

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

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Submission> submissions = new ArrayList<>();
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCases> testCases = new ArrayList<>();
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InputOutput> inputOutput = new ArrayList<>();

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
