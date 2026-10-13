package minicode.com.repository;

import jakarta.persistence.LockModeType;
import minicode.com.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    // Spring Data JPA reads the method name and generates the query when the application starts
    Optional<User> findFirstByUserName(String userName);

    Optional<User> findFirstByEmail(String email);

    /** Fetches only the bio of one user by their id; empty when unset. */
    @Query("select u.bio from User u where u.userId = :userId")
    Optional<String> findBioById(@Param("userId") UUID userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update User u set u.tokenUsage = u.tokenUsage + :tokens where u.userId = :userId")
    void addTokenUsage(@Param("userId") UUID userId, @Param("tokens") long tokens);

    /**
     * Grows the score and the solved-counters of one accepted solve in a
     * single UPDATE statement, so concurrent solves can never overwrite
     * each other's award the way a load-then-save would.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
            update User u set
                u.score = u.score + :score,
                u.numberOfEasyChallengesSolved = u.numberOfEasyChallengesSolved + :easySolved,
                u.numberOfMediumChallengesSolved = u.numberOfMediumChallengesSolved + :mediumSolved,
                u.numberOfHardChallengesSolved = u.numberOfHardChallengesSolved + :hardSolved
            where u.userId = :userId
            """)
    void addSolveAward(
            @Param("userId") UUID userId,
            @Param("score") int score,
            @Param("easySolved") int easySolved,
            @Param("mediumSolved") int mediumSolved,
            @Param("hardSolved") int hardSolved);

    /**
     * SELECT ... FOR UPDATE: the caller holds the user row until its
     * transaction commits, serializing every solve-recording of that user.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.userId = :userId")
    Optional<User> findByIdForUpdate(@Param("userId") UUID userId);
}