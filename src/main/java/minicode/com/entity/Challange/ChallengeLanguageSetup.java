package minicode.com.entity.Challange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Per-language authoring data for a challenge: the starter template shown to
 * the solver and the runner the server prepends/appends before compiling.
 *
 * <p>A challenge can be solved in several languages, so there is one row per
 * {@code (challenge, language)} pair. The runner is challenge-specific (it
 * calls the challenge's function with the right signature), which is why it
 * cannot be derived from the language alone.
 */
@Entity
@Table(
        name = "challenge_language_setup",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_challenge_language",
                columnNames = {"challenge_id", "language"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeLanguageSetup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    /** Canonical, lower-case language key: python, java, csharp, cplusplus. */
    @Column(nullable = false)
    private String language;

    @Column(columnDefinition = "TEXT")
    private String template;

    @Column(columnDefinition = "TEXT")
    private String templateExplanation;

    @Column(columnDefinition = "TEXT")
    private String runner;

    @Column(columnDefinition = "TEXT")
    private String runnerExplanation;
}
