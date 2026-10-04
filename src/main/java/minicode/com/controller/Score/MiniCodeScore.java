package minicode.com.controller.Score;

import minicode.com.controller.Score.Diffculty.DiffcultyMultiplier;

public class MiniCodeScore implements ScoreStrat{
    @Override
    public int calculateScore(int totalTestCases, int passedTestCases, DiffcultyMultiplier diffcultyMultiplier, String difficultyLevel) {
        int baseScore = (passedTestCases * 100) / totalTestCases;
        int multiplier = diffcultyMultiplier.getMultiplier();
        return baseScore * multiplier;
    }
}
