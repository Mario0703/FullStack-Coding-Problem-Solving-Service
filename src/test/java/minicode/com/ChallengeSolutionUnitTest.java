package minicode.com;

import minicode.com.controller.Challenges.ChallengeController;
import minicode.com.entity.Challange.Challenge;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import minicode.com.service.Challange.ChallengeService;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChallengeSolutionUnitTest {

    private static final String SOLUTION = "def two_sum(nums, target):\n    return [0, 1]";

    private ChallengeService challengeService;
    private ChallengeController challengeController;

    @BeforeEach
    public void setUp() {
        challengeService = mock(ChallengeService.class);
        challengeController = new ChallengeController(
                challengeService,
                mock(UserService.class),
                mock(SubmissionRepo.class),
                mock(UserRepo.class)
        );
    }

    @Test
    public void givenAnyVisitorWithoutAuthenticationWhenTheInstructorSolutionIsRequestedThenTheSolutionIsReturned() {
        // given a challenge with an instructor solution
        UUID challengeId = UUID.randomUUID();
        Challenge challenge = new Challenge();
        challenge.setSolution(SOLUTION);
        when(challengeService.getChallenge(challengeId)).thenReturn(challenge);

        // when the instructor solution is requested without any authentication
        String solution = challengeController.getChallengeSolution(challengeId);

        // then the solution is returned to the visitor
        assertEquals(SOLUTION, solution);
    }
}