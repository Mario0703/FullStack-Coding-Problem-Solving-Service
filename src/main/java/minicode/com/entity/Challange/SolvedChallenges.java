package minicode.com.entity.Challange;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minicode.com.entity.User.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "solved_challenges")
public class SolvedChallenges {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID SolvedChallengeId;

    @Column
    private UUID userId;

    @Column
    private UUID challengeId;

    @Column
    private String code;

    @Column
    private String language;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant solvedAt;

    public SolvedChallenges(Challenge challenge, User user, String code, String language) {
        this.userId = user.getUserId();
        this.challengeId = challenge.getId();
        this.code = code;
        this.language = language;
    }
}
