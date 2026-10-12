package minicode.com;

import minicode.com.entity.Challange.Difficulty;
import minicode.com.repository.UserRepo;
import minicode.com.service.scoreService.ScoreService;
import minicode.com.service.scoreService.factory.DifficultyFactory;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ScoreServiceUnitTest {

    @Test
    public void givenAnAcceptedHardSolveWhenTheScoreIsAwardedThenScoreAndCountersGrowByOneAtomicUpdate() {
        // given a user and their accepted hard solve
        UUID userId = UUID.randomUUID();
        UserRepo userRepo = mock(UserRepo.class);
        ScoreService scoreService = new ScoreService(userRepo);

        // when the score is awarded for it
        scoreService.updateScoreAndSolvedCounters(userId, 10, 10, "HARD");

        // then the score and the hard counter grow by one atomic update statement,
        // with no read-modify-write window another concurrent solve could slip into
        verify(userRepo).addSolveAward(userId, 300, 0, 0, 1);
        verify(userRepo, never()).findById(any(UUID.class));
        verify(userRepo, never()).save(any());
    }

    @Test
    public void givenAnAcceptedEasySolveWhenTheScoreIsAwardedThenScoreAndCountersGrowByOneAtomicUpdate() {
        // given a user and their accepted easy solve
        UUID userId = UUID.randomUUID();
        UserRepo userRepo = mock(UserRepo.class);
        ScoreService scoreService = new ScoreService(userRepo);

        // when the score is awarded for it
        scoreService.updateScoreAndSolvedCounters(userId, 10, 2, "EASY");

        // then the score and the easy counter grow by one atomic update statement
        verify(userRepo).addSolveAward(userId, 20, 1, 0, 0);
        verify(userRepo, never()).findById(any(UUID.class));
        verify(userRepo, never()).save(any());
    }

    @Test
    public void givenADifficultSolveWhenTheScoreIsAwardedThenOnlyItsOwnCounterGrows() {
        // given a medium solve
        UUID userId = UUID.randomUUID();
        UserRepo userRepo = mock(UserRepo.class);
        ScoreService scoreService = new ScoreService(userRepo);

        // when the score is awarded for it
        scoreService.updateScoreAndSolvedCounters(userId, 10, 10, "MEDIUM");

        // then only the medium counter grows
        ArgumentCaptor<Integer> medium = ArgumentCaptor.forClass(Integer.class);
        verify(userRepo).addSolveAward(any(UUID.class), anyInt(), anyInt(), medium.capture(), anyInt());
        assertEquals(1, medium.getValue());
    }

    @Test
    public void testDifficultyFactoryCreatesDifficultyIgnoringCase() {
        assertEquals(Difficulty.EASY, DifficultyFactory.createDifficulty("easy"));
        assertEquals(Difficulty.MEDIUM, DifficultyFactory.createDifficulty("MEDIUM"));
        assertEquals(Difficulty.HARD, DifficultyFactory.createDifficulty("Hard"));
    }

    @Test
    public void testDifficultyFactoryThrowsForNullDifficulty() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> DifficultyFactory.createDifficulty(null)
        );
        assertEquals("Difficulty level is required", exception.getMessage());
    }

    @Test
    public void testDifficultyFactoryThrowsForInvalidDifficulty() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> DifficultyFactory.createDifficulty("UNKNOWN")
        );
        assertEquals("Invalid difficulty level: UNKNOWN", exception.getMessage());
    }
}