package minicode.com;

import minicode.com.controller.Challenges.submissionController;
import minicode.com.dto.User.Response.SolvedChallengeResponse;
import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubmissionControllerUnitTest {

    @Test
    public void testListSolvedChallengesReturnsOneEntryPerChallenge() {
        UUID userId = UUID.randomUUID();
        UUID addChallengeId = UUID.randomUUID();
        UUID reverseChallengeId = UUID.randomUUID();

        Instant olderSolve = Instant.parse("2026-10-01T10:00:00Z");
        Instant newerSolve = Instant.parse("2026-10-02T10:00:00Z");
        Instant latestSolve = Instant.parse("2026-10-03T10:00:00Z");

        // The repo returns one row per (challenge, language), most recent
        // solve first: re-solving the same challenge never adds entries.
        List<SolvedChallenges> solvedRows = List.of(
                solvedChallenge(userId, reverseChallengeId, "csharp", latestSolve),
                solvedChallenge(userId, addChallengeId, "java", newerSolve),
                solvedChallenge(userId, addChallengeId, "python", olderSolve)
        );

        Challenge addChallenge = challenge(addChallengeId, "Add Two Numbers", Difficulty.EASY);
        Challenge reverseChallenge = challenge(reverseChallengeId, "Reverse String", Difficulty.MEDIUM);

        User user = new User();
        user.setUserName("TestUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setUserId(userId);

        solvedChallengesRepo mockSolvedRepo = mock(solvedChallengesRepo.class);
        when(mockSolvedRepo.findAllByUserId(userId)).thenReturn(solvedRows);

        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);
        when(mockChallengeRepo.findAllById(any())).thenReturn(List.of(addChallenge, reverseChallenge));

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findFirstByUserName("TestUser")).thenReturn(Optional.of(user));
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "TestUser",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        submissionController controller = new submissionController(
                mock(SubmissionRepo.class),
                mockUserRepo,
                mockSolvedRepo,
                mockChallengeRepo
        );

        List<SolvedChallengeResponse> solvedChallenges = controller.listSolvedChallenges(authentication);

        // Two solved challenges, not three rows: each challenge appears once.
        assertEquals(2, solvedChallenges.size());

        SolvedChallengeResponse first = solvedChallenges.get(0);
        assertEquals(reverseChallengeId, first.challengeId());
        assertEquals("Reverse String", first.challengeName());
        assertEquals("MEDIUM", first.difficulty());
        assertEquals(latestSolve, first.solvedAt());
        assertEquals(List.of("csharp"), first.languages());

        // Solved in two languages: one entry with both languages and the
        // most recent solve date.
        SolvedChallengeResponse second = solvedChallenges.get(1);
        assertEquals(addChallengeId, second.challengeId());
        assertEquals("Add Two Numbers", second.challengeName());
        assertEquals("EASY", second.difficulty());
        assertEquals(newerSolve, second.solvedAt());
        assertEquals(List.of("java", "python"), second.languages());
    }

    @Test
    public void testListSolvedChallengesSkipsRowsForDeletedChallenges() {
        UUID userId = UUID.randomUUID();
        UUID missingChallengeId = UUID.randomUUID();

        solvedChallengesRepo mockSolvedRepo = mock(solvedChallengesRepo.class);
        when(mockSolvedRepo.findAllByUserId(userId)).thenReturn(List.of(
                solvedChallenge(userId, missingChallengeId, "python", Instant.now())
        ));

        ChallengeRepo mockChallengeRepo = mock(ChallengeRepo.class);
        when(mockChallengeRepo.findAllById(any())).thenReturn(List.of());

        User user = new User();
        user.setUserName("TestUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        user.setUserId(userId);

        UserRepo mockUserRepo = mock(UserRepo.class);
        when(mockUserRepo.findFirstByUserName("TestUser")).thenReturn(Optional.of(user));
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "TestUser",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        submissionController controller = new submissionController(
                mock(SubmissionRepo.class),
                mockUserRepo,
                mockSolvedRepo,
                mockChallengeRepo
        );

        assertEquals(List.of(), controller.listSolvedChallenges(authentication));
    }

    private SolvedChallenges solvedChallenge(UUID userId, UUID challengeId, String language, Instant solvedAt) {
        SolvedChallenges solved = new SolvedChallenges();
        solved.setUserId(userId);
        solved.setChallengeId(challengeId);
        solved.setLanguage(language);
        solved.setSolvedAt(solvedAt);
        return solved;
    }

    private Challenge challenge(UUID challengeId, String name, Difficulty difficulty) {
        Challenge challenge = new Challenge();
        challenge.setName(name);
        challenge.setDescription("description");
        challenge.setDifficulty(difficulty);
        challenge.setCategory(Category.GENERAL);
        challenge.setNumberOfTestCases(2);
        challenge.setSolution("solution");
        challenge.setRunner("runner");
        challenge.setRunnerExplanation("runnerExplanation");
        challenge.setTemplete("template");
        challenge.setTempleteExplanation("templateExplanation");
        challenge.setLanguage("python");
        challenge.setId(challengeId);
        return challenge;
    }
}