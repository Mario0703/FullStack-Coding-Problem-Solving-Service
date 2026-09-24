package minicode.com.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Challenges", schema = "public") // provides the name of the table

public class Challenge {
    protected Challenge() {
    }

    @Column(nullable = false, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Column(nullable = false)
    private String description;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    public Challenge(String name, String description, Difficulty difficulty, Category category) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
    }


}
