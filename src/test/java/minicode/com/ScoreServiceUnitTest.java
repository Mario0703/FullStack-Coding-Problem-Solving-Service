package minicode.com;

import minicode.com.controller.Score.Diffculty.DiffcultyFacttory;
import minicode.com.controller.Score.MiniCodeScore;
import minicode.com.controller.Score.ScoreStrat;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.UserRepo;
import minicode.com.service.scoreService.ScoreService;
import minicode.com.service.scoreService.factory.DifficultyFactory;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ScoreServiceUnitTest {

    private ScoreService createScoreService(UserRepo userRepo, ChallengeRepo challengeRepo) {
        return new ScoreService(userRepo, challengeRepo);
    }

    private User createUser() {
        User user = new User();
        user.setUserName("TestUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setScore(0);
        user.setNumberOfEasyChallengesSolved(0);
        user.setNumberOfMediumChallengesSolved(0);
        user.setNumberOfHardChallengesSolved(0);
        return user;
    }

    @Test
    public void testUpdateNumberOfChallengesSolvedForEasyDifficulty() {
        UUID userId = UUID.randomUUID();
        User user = createUser();

        UserRepo mockUserRepo = mock(UserRepo.class);
        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScoreService scoreService = createScoreService(mockUserRepo, mockChallengeRepo);
        scoreService.updateNumberOfChallengesSolvedBasedOnDifficulty(userId, "EASY");

        assertEquals(1, user.getNumberOfEasyChallengesSolved());
        assertEquals(0, user.getNumberOfMediumChallengesSolved());
        assertEquals(0, user.getNumberOfHardChallengesSolved());
        verify(mockUserRepo).save(user);
    }

    @Test
    public void testUpdateNumberOfChallengesSolvedForMediumDifficulty() {
        UUID userId = UUID.randomUUID();
        User user = createUser();

        UserRepo mockUserRepo = mock(UserRepo.class);
        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScoreService scoreService = createScoreService(mockUserRepo, mockChallengeRepo);
        scoreService.updateNumberOfChallengesSolvedBasedOnDifficulty(userId, "medium");

        assertEquals(0, user.getNumberOfEasyChallengesSolved());
        assertEquals(1, user.getNumberOfMediumChallengesSolved());
        assertEquals(0, user.getNumberOfHardChallengesSolved());
        verify(mockUserRepo).save(user);
    }

    @Test
    public void testUpdateNumberOfChallengesSolvedForHardDifficulty() {
        UUID userId = UUID.randomUUID();
        User user = createUser();

        UserRepo mockUserRepo = mock(UserRepo.class);
        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScoreService scoreService = createScoreService(mockUserRepo, mockChallengeRepo);
        scoreService.updateNumberOfChallengesSolvedBasedOnDifficulty(userId, "Hard");

        assertEquals(0, user.getNumberOfEasyChallengesSolved());
        assertEquals(0, user.getNumberOfMediumChallengesSolved());
        assertEquals(1, user.getNumberOfHardChallengesSolved());
        verify(mockUserRepo).save(user);
    }

    @Test
    public void testUpdateNumberOfChallengesSolvedForInvalidDifficultyThrows() {
        UUID userId = UUID.randomUUID();

        UserRepo mockUserRepo = mock(UserRepo.class);
        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);

        ScoreService scoreService = createScoreService(mockUserRepo, mockChallengeRepo);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> scoreService.updateNumberOfChallengesSolvedBasedOnDifficulty(userId, "IMPOSSIBLE")
        );

        assertEquals("Invalid difficulty level: IMPOSSIBLE", exception.getMessage());
        verify(mockUserRepo, never()).findById(any());
    }

    @Test
    public void testUpdateScoreAndSolvedCountersAddsScoreAndIncrementsCounter() {
        UUID userId = UUID.randomUUID();
        User user = createUser();

        UserRepo mockUserRepo = mock(UserRepo.class);
        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScoreService scoreService = createScoreService(mockUserRepo, mockChallengeRepo);
        scoreService.updateScoreAndSolvedCounters(userId, 10, 10, "HARD");

        assertEquals(300, user.getScore());
        assertEquals(0, user.getNumberOfEasyChallengesSolved());
        assertEquals(0, user.getNumberOfMediumChallengesSolved());
        assertEquals(1, user.getNumberOfHardChallengesSolved());
        verify(mockUserRepo).save(user);
    }

    @Test
    public void testUpdateScoreAndSolvedCountersDoesNotGoNegative() {
        UUID userId = UUID.randomUUID();
        User user = createUser();

        UserRepo mockUserRepo = mock(UserRepo.class);
        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockUserRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScoreService scoreService = createScoreService(mockUserRepo, mockChallengeRepo);
        scoreService.updateScoreAndSolvedCounters(userId, 10, 2, "EASY");

        assertEquals(20, user.getScore());
        assertEquals(1, user.getNumberOfEasyChallengesSolved());
        verify(mockUserRepo).save(user);
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
