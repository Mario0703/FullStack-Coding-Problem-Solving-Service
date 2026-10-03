package minicode.com.entity.Challange;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minicode.com.entity.User.User;

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

    public SolvedChallenges(Challenge challenge, User user, String code) {
        this.userId = user.getUserId();
        this.challengeId = challenge.getId();
        this.code = code;
    }
}
