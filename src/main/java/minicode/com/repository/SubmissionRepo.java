package minicode.com.repository;

import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubmissionRepo extends JpaRepository<Submission, UUID> {

    java.util.Optional<Submission> findByIdAndUser_UserId(UUID id, UUID userId);

    List<Submission> findAllByUser_UserId(UUID userId);

    void deleteAllByUser_UserId(UUID userId);

    long countByUser_UserId(UUID userId);

    // The submission receipt: the user's failed attempts. Which challenges are
    // solved comes from the SolvedChallenges table instead, so re-solving a
    // challenge never duplicates solved entries in the receipt. All four
    // queries filter the same statuses in the same order because the
    // dashboard aligns them by index.
    @Query("""
    SELECT s
    FROM Submission s
    WHERE s.user.userId = :userId
      AND s.status IN ('WRONG_ANSWER', 'COMPILE_ERROR', 'RUNTIME_ERROR')
    ORDER BY s.createdAt DESC
    """)
    List<Submission> findReceiptByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT s.testCasesPassed
    FROM Submission s
    WHERE s.user.userId = :userId
      AND s.status IN ('WRONG_ANSWER', 'COMPILE_ERROR', 'RUNTIME_ERROR')
    ORDER BY s.createdAt DESC
    """)
    List<Integer> findReceiptPassedTestCasesByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT s.testCasesFailed
    FROM Submission s
    WHERE s.user.userId = :userId
      AND s.status IN ('WRONG_ANSWER', 'COMPILE_ERROR', 'RUNTIME_ERROR')
    ORDER BY s.createdAt DESC
    """)
    List<Integer> findReceiptFailedTestCasesByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT c.numberOfTestCases
    FROM Submission s
    INNER JOIN s.challenge c
    WHERE s.user.userId = :userId
      AND s.status IN ('WRONG_ANSWER', 'COMPILE_ERROR', 'RUNTIME_ERROR')
    ORDER BY s.createdAt DESC
    """)
    List<Integer> findReceiptTotalTestCasesByUserId(
            @Param("userId") UUID userId
    );

    @Query("""
    SELECT s
    FROM Submission s
    WHERE s.challenge.id = :challengeId
      AND s.status = 'ACCEPTED'
    ORDER BY s.createdAt DESC
    """)
    List<Submission> findAcceptedByChallengeId(@Param("challengeId") UUID challengeId);

    /**
     * Whether a user has an accepted submission for a challenge. Used to
     * decide if the instructor solution may be revealed to them.
     */
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

    @Query("""
    SELECT s.testCasesPassed
    FROM Submission s
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
    """)
    List<Integer> findPassedTestCasesByUserIdPublic(@Param("userId") UUID userId);

    @Query("""
    SELECT s.testCasesFailed
    FROM Submission s
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
    """)
    List<Integer> findFailedTestCasesByUserIdPublic(@Param("userId") UUID userId);

    @Query("""
    SELECT c.numberOfTestCases
    FROM Submission s
    INNER JOIN s.challenge c
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
    """)
    List<Integer> findTotalTestCasesByUserIdPublic(
            @Param("userId") UUID userId
    );
}