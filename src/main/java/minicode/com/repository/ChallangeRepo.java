package minicode.com.repository;

import minicode.com.entity.Challange;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallangeRepo extends JpaRepository<Challange, UUID> {

    Optional<Challange> findByName(String name);
    Optional<Challange> findById(UUID uuid);


}
