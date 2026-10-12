package minicode.com;

import minicode.com.dto.User.Response.UserStatsResponse;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import minicode.com.service.scoreService.ScoreBoardService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScoreBoardServiceUnitTest {

    @Test
    public void givenRegisteredUsersWhenTheUserStatsAreLoadedThenEachEntryCarriesItsUserIdAndName() {
        // given two registered users with their solve counts
        UUID firstUserId = UUID.randomUUID();
        UUID secondUserId = UUID.randomUUID();
        User first = user(firstUserId, "FirstUser", 120, 3, 2, 1);
        User second = user(secondUserId, "SecondUser", 80, 1, 0, 0);

        UserRepo userRepo = mock(UserRepo.class);
        when(userRepo.findAll()).thenReturn(List.of(first, second));

        // when the scoreboard user stats are loaded
        List<UserStatsResponse> stats = new ScoreBoardService(userRepo).getUserStats();

        // then each entry carries its own user id and name, so the
        // scoreboard can link each row to its public profile
        assertEquals(2, stats.size());
        assertEquals(firstUserId, stats.get(0).userId());
        assertEquals("FirstUser", stats.get(0).userName());
        assertEquals(120, stats.get(0).score());
        assertEquals(3, stats.get(0).numberOfEasyChallengesSolved());
        assertEquals(2, stats.get(0).numberOfMediumChallengesSolved());
        assertEquals(1, stats.get(0).numberOfHardChallengesSolved());
        assertEquals(6, stats.get(0).totalChallengesSolved());

        assertEquals(secondUserId, stats.get(1).userId());
        assertEquals("SecondUser", stats.get(1).userName());
        assertEquals(80, stats.get(1).score());
        assertEquals(1, stats.get(1).totalChallengesSolved());
    }

    private User user(UUID userId, String userName, int score, int easy, int medium, int hard) {
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setEmail(userName.toLowerCase() + "@example.com");
        user.setPassword("encoded-password");
        user.setRole(UserRole.USER);
        user.setScore(score);
        user.setNumberOfEasyChallengesSolved(easy);
        user.setNumberOfMediumChallengesSolved(medium);
        user.setNumberOfHardChallengesSolved(hard);
        return user;
    }
}