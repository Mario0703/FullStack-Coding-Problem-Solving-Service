package minicode.com.controller.Score;

import minicode.com.controller.Score.Diffculty.DiffcultyMultiplier;

public interface ScoreStrat {
    int calculateScore(int totalTestCases, int passedTestCases, DiffcultyMultiplier diffcultyMultiplier, String difficultyLevel);
}
