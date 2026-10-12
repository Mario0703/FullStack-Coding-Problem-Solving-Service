package minicode.com.entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.Roles.UserRole;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity // This class should be tracked as a table
@Table(name = "users", schema = "public") // provides the name of the table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(columnDefinition = "integer default 0")
    private int score = 0;

    @Column(columnDefinition = "integer default 0")
    private int numberOfEasyChallengesSolved = 0;

    @Column(columnDefinition = "integer default 0")
    private int numberOfMediumChallengesSolved = 0;

    @Column(columnDefinition = "integer default 0")
    private int numberOfHardChallengesSolved = 0;

    /** Total OpenAI tokens the user has spent through miniHelper. */
    @Column(columnDefinition = "bigint default 0")
    private long tokenUsage = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    public UUID getId() {
        return userId;
    }

    public int getTotalChallengesSolved() {
        return numberOfEasyChallengesSolved
                + numberOfMediumChallengesSolved
                + numberOfHardChallengesSolved;
    }
}



