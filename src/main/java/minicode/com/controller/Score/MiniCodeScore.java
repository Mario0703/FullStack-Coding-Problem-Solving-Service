package minicode.com.controller.Score;

import minicode.com.controller.Score.Diffculty.DifficultyMultiplier;

public class MiniCodeScore implements ScoreStrat{
    @Override
    public int calculateScore(int totalTestCases, int passedTestCases, DifficultyMultiplier difficultyMultiplier, String difficultyLevel) {
        int baseScore = (passedTestCases * 100) / totalTestCases;
        int multiplier = difficultyMultiplier.getMultiplier();
        return baseScore * multiplier;
    }
}
