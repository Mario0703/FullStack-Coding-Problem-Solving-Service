package minicode.com.repository;

import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    // Spring Data JPA reads the method name and generates the query when the application starts
    Optional<User> findFirstByUserName(String userName);

    Optional<User> findFirstByEmail(String email);

    Optional<User> findFirstByUserNameAndRole(String userName, UserRole role);

}



