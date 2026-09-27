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

    public int getTestCasesPassed() {
        return testCasesPassed;
    }

    public int getTestCasesFailed() {
        return testCasesFailed;
    }

    @Column(updatable = false)
    private int testCasesPassed;

    @Column(updatable = false)
    private int testCasesFailed;

    protected Submission() {
    }

    public UUID getId() { return id; }
    public Challenge getChallenge() { return challenge; }
    public String getCode() { return code; }
    public String getLanguage() { return language; }
    public int getExitCode() { return exitCode; }
    public SubmissionStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public Submission(
            User user,
            Challenge challenge,
            String code,
            String language,
            int exitCode,
            SubmissionStatus status,
            int testCasesPassed,
            int testCasesFailed
    ) {
        this.user = user;
        this.challenge = challenge;
        this.code = code;
        this.language = language;
        this.exitCode = exitCode;
        this.status = status;
        this.testCasesPassed = testCasesPassed;
        this.testCasesFailed = testCasesFailed;
    }
}
