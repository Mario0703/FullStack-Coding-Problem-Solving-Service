package minicode.com.repository;

import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepo extends JpaRepository<Submission, UUID> {

    Optional<Submission> findFirstByUser_UserIdAndChallenge_IdOrderByCreatedAtDesc(
            UUID userId,
            UUID challengeId
    );

   Optional<Submission> findByIdAndUser_UserId(UUID id, UUID userId);

    List<Submission> findAllByUser_UserId(UUID userId);

    void deleteAllByUser_UserId(UUID userId);

    long countByUser_UserId(UUID userId);


    @Query("""
    SELECT s
    FROM Submission s
    WHERE s.user.userId = :userId
      AND s.status IN ('WRONG_ANSWER', 'COMPILE_ERROR', 'RUNTIME_ERROR')
    ORDER BY s.createdAt DESC
    """)
    List<Submission> findReceiptByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT s
    FROM Submission s
    WHERE s.challenge.id = :challengeId
      AND s.status = 'ACCEPTED'
    ORDER BY s.createdAt DESC
    """)
    List<Submission> findAcceptedByChallengeId(@Param("challengeId") UUID challengeId);


    boolean existsByUser_UserIdAndChallenge_IdAndStatus(
            UUID userId,
            UUID challengeId,
            SubmissionStatus status
    );

    @Query("""
    SELECT s
    FROM Submission s
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
    """)
    List<Submission> findPublicSubmissionsByUserId(@Param("userId") UUID userId);
}