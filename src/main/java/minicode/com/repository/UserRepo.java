package minicode.com.repository;

import minicode.com.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
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


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update User u set u.tokenUsage = u.tokenUsage + :tokens where u.userId = :userId")
    void addTokenUsage(@Param("userId") UUID userId, @Param("tokens") long tokens);

}



