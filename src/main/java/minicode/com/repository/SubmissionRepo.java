package minicode.com.repository;

import minicode.com.entity.Submission;
import minicode.com.entity.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubmissionRepo extends JpaRepository<Submission, UUID> {

    java.util.Optional<Submission> findByIdAndUser_UserId(UUID id, UUID userId);

    @Query("""
            SELECT DISTINCT submission.challenge.id
            FROM Submission submission
            WHERE submission.user.userId = :userId
              AND submission.status = :status
            """)
    List<UUID> findChallengeIdsByUserIdAndStatus(
            @Param("userId") UUID userId,
            @Param("status") SubmissionStatus status
    );
    @Query("""
    SELECT s
    FROM Submission s
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
""")
    List<Submission> findAllByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT s.testCasesPassed
    FROM Submission s
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
""")
    List<Integer> findPassedTestCasesByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT s.testCasesFailed
    FROM Submission s
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
""")
    List<Integer> findFailedTestCasesByUserId(@Param("userId") UUID userId);

    @Query("""
    SELECT c.numberOfTestCases
    FROM Submission s
    INNER JOIN s.challenge c
    WHERE s.user.userId = :userId
    ORDER BY s.createdAt DESC
""")
    List<Integer> findTotalTestCasesByUserId(
            @Param("userId") UUID userId
    );
}
