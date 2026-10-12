package minicode.com.service.scoreService.strategy;

/**
 * How much a single accepted solve grows each solved-counter. Kept as a
 * value so the award becomes one atomic UPDATE statement instead of a
 * read-modify-write on the user entity.
 */
public record SolvedCounters(int easy, int medium, int hard) {

    public static SolvedCounters forEasySolve() {
        return new SolvedCounters(1, 0, 0);
    }

    public static SolvedCounters forMediumSolve() {
        return new SolvedCounters(0, 1, 0);
    }

    public static SolvedCounters forHardSolve() {
        return new SolvedCounters(0, 0, 1);
    }
}