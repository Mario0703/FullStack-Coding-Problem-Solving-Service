package minicode.com.entity.Challange;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import minicode.com.entity.Submissions.Submission;

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

    // Never serialized: submission history would leak other users' code and
    // test cases would leak hidden inputs/expected outputs.
    @JsonIgnore
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Submission> submissions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCases> testCases = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InputOutput> inputOutput = new ArrayList<>();

    // Per-language templates/runners. Never serialized; served through the
    // template endpoint instead.
    @JsonIgnore
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeLanguageSetup> languageSetups = new ArrayList<>();

    // Instructor solution and server-side runner: never exposed through the
    // API. The solution is served by its own guarded endpoint.
    @JsonIgnore
    @Column( columnDefinition = "TEXT")
    private String solution;

    @JsonIgnore
    @Column( columnDefinition = "TEXT")
    private String runner;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String runnerExplanation;

    @Column( columnDefinition = "TEXT")
    private String templete;


    @Column(columnDefinition = "TEXT")
    private String templeteExplanation;

    @Column
    private String language;


    public Challenge(
            String name,
            String description,
            Difficulty difficulty,
            Category category,
            int numberOfTestCases,
            String solution,
            String runner,
            String runnerExplanation,
            String templete,
            String templeteExplanation,
            String language
    ) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.numberOfTestCases = numberOfTestCases;
        this.solution = solution;
        this.runner = runner;
        this.runnerExplanation = runnerExplanation;
        this.templete = templete;
        this.templeteExplanation = templeteExplanation;
        this.language = language;
    }
}




