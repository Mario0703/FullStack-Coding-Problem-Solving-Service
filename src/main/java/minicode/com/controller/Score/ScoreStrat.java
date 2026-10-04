package minicode.com.controller.Score;

import minicode.com.controller.Score.Diffculty.DifficultyMultiplier;

public interface ScoreStrat {
    int calculateScore(int totalTestCases, int passedTestCases, DifficultyMultiplier difficultyMultiplier, String difficultyLevel);
}
