package minicode.com.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Challenges", schema = "public") // provides the name of the table

public class Challenge {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private Difficulty difficulty;
    @Column(nullable = false)
    private Catagory catagory;


}
