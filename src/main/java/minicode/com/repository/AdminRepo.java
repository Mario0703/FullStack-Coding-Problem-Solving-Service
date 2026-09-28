package minicode.com.repository;

import minicode.com.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepo extends JpaRepository<AdminUser, UUID>{
    Optional<AdminUser> findByAdminUserName(String adminUserName);

    @Query(value = "SELECT CASE WHEN (SELECT COUNT(*) FROM admin_users) = 0 THEN TRUE ELSE FALSE END", nativeQuery = true)
    boolean isThereAnStartAdmin();
}
