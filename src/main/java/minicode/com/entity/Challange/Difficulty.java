package minicode.com.entity.Challange;

import minicode.com.service.scoreService.strategy.DifficultyUpdateStrategy;
import minicode.com.service.scoreService.strategy.SolvedCounters;
import minicode.com.service.scoreService.strategy.impl.EasyDifficultyUpdateStrategy;
import minicode.com.service.scoreService.strategy.impl.HardDifficultyUpdateStrategy;
import minicode.com.service.scoreService.strategy.impl.MediumDifficultyUpdateStrategy;

public enum Difficulty {
    EASY(new EasyDifficultyUpdateStrategy()),
    MEDIUM(new MediumDifficultyUpdateStrategy()),
    HARD(new HardDifficultyUpdateStrategy());

    private final DifficultyUpdateStrategy updateStrategy;

    Difficulty(DifficultyUpdateStrategy updateStrategy) {
        this.updateStrategy = updateStrategy;
    }

    public SolvedCounters solvedCountersForOneSolve() {
        return updateStrategy.solvedCountersForOneSolve();
    }
}