package minicode.com.repository;

import minicode.com.entity.Challange.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChallengeRepo extends JpaRepository<Challenge, UUID> {
}



