package minicode.com.service.scoreService;

import minicode.com.controller.Score.Diffculty.DiffcultyFacttory;
import minicode.com.controller.Score.Diffculty.DifficultyMultiplier;
import minicode.com.controller.Score.MiniCodeScore;
import minicode.com.controller.Score.ScoreStrat;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.repository.UserRepo;
import minicode.com.service.scoreService.factory.DifficultyFactory;
import minicode.com.service.scoreService.strategy.SolvedCounters;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ScoreService {
    private final UserRepo userRepo;
    private final ScoreStrat scoreStrategy;
    private final DiffcultyFacttory diffcultyFacttory;

    public ScoreService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.scoreStrategy = new MiniCodeScore();
        this.diffcultyFacttory = new DiffcultyFacttory();
    }


    public void updateScoreAndSolvedCounters(UUID userId, int totalTestCases, int passedTestCases, String difficultyLevel) {
        Difficulty difficulty = DifficultyFactory.createDifficulty(difficultyLevel);

        DifficultyMultiplier multiplierStrategy = diffcultyFacttory.getDiffcultyMultiplier(difficultyLevel).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Difficulty multiplier not found"));

        int score = scoreStrategy.calculateScore(totalTestCases, passedTestCases, multiplierStrategy, difficultyLevel);
        SolvedCounters solvedCounters = difficulty.solvedCountersForOneSolve();

        userRepo.addSolveAward(
                userId,
                score,
                solvedCounters.easy(),
                solvedCounters.medium(),
                solvedCounters.hard()
        );
    }
}