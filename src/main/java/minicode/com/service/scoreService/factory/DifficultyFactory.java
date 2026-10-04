package minicode.com.service.scoreService.factory;

import minicode.com.entity.Challange.Difficulty;

import java.util.HashMap;
import java.util.Map;

public class DifficultyFactory {
    private static final Map<String, Difficulty> DIFFICULTY_REGISTRY = new HashMap<>();

    static {
        for (Difficulty difficulty : Difficulty.values()) {
            DIFFICULTY_REGISTRY.put(difficulty.name(), difficulty);
        }
    }

    private DifficultyFactory() {
    }

    public static Difficulty createDifficulty(String difficultyLevel) {
        if (difficultyLevel == null || difficultyLevel.isBlank()) {
            throw new RuntimeException("Difficulty level is required");
        }

        Difficulty difficulty = DIFFICULTY_REGISTRY.get(difficultyLevel.toUpperCase());
        if (difficulty == null) {
            throw new RuntimeException("Invalid difficulty level: " + difficultyLevel);
        }

        return difficulty;
    }
}
