package minicode.com.repository;

import minicode.com.entity.Container.Container;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerRepo extends JpaRepository<Container, String> {
}
