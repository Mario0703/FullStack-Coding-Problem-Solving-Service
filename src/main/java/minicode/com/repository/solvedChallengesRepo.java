package minicode.com.repository;

import minicode.com.entity.Challange.SolvedChallenges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface solvedChallengesRepo extends JpaRepository<SolvedChallenges, UUID> {
    List<SolvedChallenges> findAllByChallengeId(UUID challengeId);

    boolean existsByUserIdAndChallengeId(UUID userId, UUID challengeId);
}
