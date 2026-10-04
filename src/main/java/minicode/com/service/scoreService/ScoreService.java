package minicode.com.service.scoreService;

import lombok.Setter;
import minicode.com.controller.Score.Diffculty.DiffcultyFacttory;
import minicode.com.controller.Score.Diffculty.DiffcultyMultiplier;
import minicode.com.controller.Score.MiniCodeScore;
import minicode.com.controller.Score.ScoreStrat;
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
    private final ScoreStrat scoreStrategy;
    private final DiffcultyFacttory diffcultyFacttory;

    public ScoreService(UserRepo userRepo, ChallengeRepo challengeRepo) {
        this(userRepo, challengeRepo, new MiniCodeScore(), new DiffcultyFacttory());
    }

    public ScoreService(UserRepo userRepo, ChallengeRepo challengeRepo, ScoreStrat scoreStrategy, DiffcultyFacttory diffcultyFacttory) {
        this.userRepo = userRepo;
        this.challengeRepo = challengeRepo;
        this.scoreStrategy = scoreStrategy;
        this.diffcultyFacttory = diffcultyFacttory;
    }

    public void updateNumberOfChallengesSolvedBasedOnDifficulty(UUID userId, String difficultylevel) {
        Difficulty difficulty = DifficultyFactory.createDifficulty(difficultylevel);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        difficulty.applyUpdate(user);
        userRepo.save(user);
    }

    public void updateScoreAndSolvedCounters(UUID userId, int totalTestCases, int passedTestCases, String difficultyLevel) {
        Difficulty difficulty = DifficultyFactory.createDifficulty(difficultyLevel);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        DiffcultyMultiplier multiplierStrategy = diffcultyFacttory.getDiffcultyMultiplier(difficultyLevel).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Difficulty multiplier not found"));

        int score = scoreStrategy.calculateScore(totalTestCases, passedTestCases, multiplierStrategy, difficultyLevel);

        user.setScore(user.getScore() + score);
        difficulty.applyUpdate(user);
        userRepo.save(user);
    }
}
