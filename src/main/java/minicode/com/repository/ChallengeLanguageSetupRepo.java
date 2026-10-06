package minicode.com.repository;

import minicode.com.entity.Challange.ChallengeLanguageSetup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeLanguageSetupRepo extends JpaRepository<ChallengeLanguageSetup, UUID> {

    List<ChallengeLanguageSetup> findByChallenge_Id(UUID challengeId);

    Optional<ChallengeLanguageSetup> findByChallenge_IdAndLanguage(UUID challengeId, String language);
}
