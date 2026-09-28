package minicode.com.repository;

import minicode.com.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepo extends JpaRepository<AdminUser, UUID>{
    Optional<AdminUser> findByAdminUserName(String adminUserName);
}
