package minicode.com.repository;

import io.lettuce.core.dynamic.annotation.Param;
import minicode.com.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ChallengeRepo extends JpaRepository<Challenge, UUID> {
}
