package minicode.com.entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
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
public class User {

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    private int score;

    @Column
    private int numberOfEasyChallengesSolved;

    @Column
    private int numberOfMediumChallengesSolved;

    @Column
    private int numberOfHardChallengesSolved;

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


    protected User() {
    }

    public User(String userName, String email, String password) {
        this(userName, email, password, UserRole.USER);
    }

    public User(String userName, String email, String password, UserRole role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return userId;
    }
}



