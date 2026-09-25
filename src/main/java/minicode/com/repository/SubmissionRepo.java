package minicode.com.repository;

import minicode.com.entity.Submission;
import minicode.com.entity.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubmissionRepo extends JpaRepository<Submission, UUID> {

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
}
