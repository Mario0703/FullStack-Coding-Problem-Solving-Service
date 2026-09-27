package minicode.com.service;

import minicode.com.dto.CreateChallangeRequest;
import minicode.com.entity.Category;
import minicode.com.entity.Challenge;
import minicode.com.entity.Difficulty;
import minicode.com.repository.ChallengeRepo;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {
    private final ChallengeRepo challengeRepo;

    public ChallengeService(ChallengeRepo challengeRepo) {
        this.challengeRepo = challengeRepo;
    }

    public Iterable<Challenge> listChallenges() {
        return challengeRepo.findAll();
    }

    public Challenge createChallenge(CreateChallangeRequest request) {
        String name = request.getName();
        String description = request.getDescription();
        Difficulty difficulty = request.getDifficulty();
        Category category = request.getCategory();
        int numberOfTestCases = request.getTestCases().size();

        Challenge challenge = new Challenge(
                name,
                description,
                difficulty,
                category,
                numberOfTestCases
        );

        return challengeRepo.save(challenge);
    }




}
