package minicode.com.service.scoreService.strategy.impl;

import minicode.com.service.scoreService.strategy.DifficultyUpdateStrategy;
import minicode.com.service.scoreService.strategy.SolvedCounters;

public class EasyDifficultyUpdateStrategy implements DifficultyUpdateStrategy {
    @Override
    public SolvedCounters solvedCountersForOneSolve() {
        return SolvedCounters.forEasySolve();
    }
}