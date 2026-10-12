package minicode.com.service.scoreService.strategy;

@FunctionalInterface
public interface DifficultyUpdateStrategy {

    SolvedCounters solvedCountersForOneSolve();
}