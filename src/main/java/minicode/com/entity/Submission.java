package minicode.com.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "submissions",
        indexes = {
                @Index(name = "idx_submission_user_challenge",
                        columnList = "user_id, challenge_id")
        }
)
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private int exitCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(
            name = "number_of_tests_cases",
            nullable = false,
            updatable = false,
            columnDefinition = "integer default 0"
    )
    private int numberOfTestCases;

    protected Submission() {
    }

    public Submission(
            User user,
            Challenge challenge,
            String code,
            String language,
            int exitCode,
            SubmissionStatus status,
            int numberOfTestsCases
    ) {
        this.user = user;
        this.challenge = challenge;
        this.code = code;
        this.language = language;
        this.exitCode = exitCode;
        this.status = status;
        this.numberOfTestCases = numberOfTestCases;
    }
}
