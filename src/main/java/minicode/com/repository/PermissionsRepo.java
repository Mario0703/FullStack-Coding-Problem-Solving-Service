package minicode.com.repository;

import minicode.com.entity.Roles.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionsRepo extends JpaRepository<Permissions, UUID> {
    Optional<Permissions> findByName(String name);
}



