package minicode.com.repository;

import minicode.com.entity.Challange.SolvedChallenges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface solvedChallengesRepo extends JpaRepository<SolvedChallenges, UUID> {
    List<SolvedChallenges> findAllByChallengeId(UUID challengeId);

    boolean existsByUserIdAndChallengeId(UUID userId, UUID challengeId);

    boolean existsByUserIdAndChallengeIdAndLanguage(UUID userId, UUID challengeId, String language);

    void deleteAllByUserId(UUID userId);

    // One row per (user, challenge, language); the caller groups by challenge
    // so each solved challenge is reported exactly once.
    @Query("""
            SELECT s
            FROM SolvedChallenges s
            WHERE s.userId = :userId
            ORDER BY s.solvedAt DESC
            """)
    List<SolvedChallenges> findAllByUserId(@Param("userId") UUID userId);

    @Query("""
            SELECT DISTINCT s.challengeId
            FROM SolvedChallenges s
            WHERE s.userId = :userId
            """)
    List<UUID> findChallengeIdsByUserId(@Param("userId") UUID userId);

    @Query("""
            SELECT COUNT(DISTINCT s.challengeId)
            FROM SolvedChallenges s
            """)
    long countDistinctChallenges();

    @Query("""
            SELECT COUNT(DISTINCT s.challengeId)
            FROM SolvedChallenges s
            WHERE s.userId = :userId
            """)
    long countDistinctChallengesByUserId(@Param("userId") UUID userId);
}