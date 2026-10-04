package minicode.com.service.scoreService;

import lombok.Setter;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.User.User;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.UserRepo;
import minicode.com.service.scoreService.factory.DifficultyFactory;

import java.util.UUID;

@Setter
public class ScoreService {
    private final UserRepo userRepo;
    private final ChallengeRepo challengeRepo;

    public ScoreService(UserRepo userRepo, ChallengeRepo challengeRepo) {
        this.userRepo = userRepo;
        this.challengeRepo = challengeRepo;
    }

    public void updateScore(UUID userId, int score) {
        var user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setScore(user.getScore() + score);
        userRepo.save(user);
    }

    public void updateNumberOfChallangesSolvedBasedOnDifficulty(UUID userId, String difficultylevel) {
        Difficulty difficulty = DifficultyFactory.createDifficulty(difficultylevel);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        difficulty.applyUpdate(user);
        userRepo.save(user);
    }
}
