package minicode.com.controller.Score;

import minicode.com.controller.Score.Diffculty.DiffcultyMultiplier;

public class MiniCodeScore implements ScoreStrat{
    @Override
    public int calculateScore(int totalTestCases, int passedTestCases, DiffcultyMultiplier diffcultyMultiplier, String difficultyLevel) {
        int multiplier = diffcultyMultiplier.getMultiplier(difficultyLevel);
        int score = (passedTestCases * multiplier) - (totalTestCases - passedTestCases);
        return Math.max(score, 0); // Ensure score is not negative
    }
}
