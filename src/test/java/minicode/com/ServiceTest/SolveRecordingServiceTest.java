package minicode.com.ServiceTest;

import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import minicode.com.service.Challange.AcceptedSubmission;
import minicode.com.service.Challange.SolveRecordingService;
import minicode.com.service.scoreService.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SolveRecordingServiceTest {

    private static final String LANGUAGE = "python";
    private static final String DIFFICULTY = "HARD";

    private UUID userId;
    private UUID challengeId;
    private UserRepo userRepo;
    private solvedChallengesRepo solvedChallengesRepository;
    private ScoreService scoreService;
    private SolveRecordingService solveRecordingService;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        challengeId = UUID.randomUUID();

        userRepo = mock(UserRepo.class);
        solvedChallengesRepository = mock(solvedChallengesRepo.class);
        scoreService = mock(ScoreService.class);
        solveRecordingService = new SolveRecordingService(
                userRepo, solvedChallengesRepository, scoreService);

        User user = new User();
        user.setUserId(userId);
        user.setUserName("Solver");
        user.setEmail("solver@example.com");
        user.setPassword("encoded-password");
        user.setRole(UserRole.USER);
        when(userRepo.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
    }

    @Test
    public void givenALockedUserWithAFirstSolveWhenTheAcceptedSubmissionIsRecordedThenTheRowIsLockedBeforeAnyCheckAndTheSolveIsSavedAndScored() {
        // given a user whose accepted submission solves a challenge for the first time
        when(solvedChallengesRepository.existsByUserIdAndChallengeId(userId, challengeId)).thenReturn(false);
        when(solvedChallengesRepository.existsByUserIdAndChallengeIdAndLanguage(userId, challengeId, LANGUAGE))
                .thenReturn(false);

        // when the accepted submission is recorded
        solveRecordingService.recordAcceptedSubmission(acceptedSubmission());

        // then the user row is locked before any check, so a concurrent
        // submission of the same user cannot slip between check and insert
        InOrder lockBeforeChecks = inOrder(userRepo, solvedChallengesRepository, scoreService);
        lockBeforeChecks.verify(userRepo).findByIdForUpdate(userId);
        lockBeforeChecks.verify(solvedChallengesRepository).existsByUserIdAndChallengeId(userId, challengeId);

        // and the solve is saved with the submitted code and awarded
        ArgumentCaptor<minicode.com.entity.Challange.SolvedChallenges> savedRow =
                ArgumentCaptor.forClass(minicode.com.entity.Challange.SolvedChallenges.class);
        verify(solvedChallengesRepository).save(savedRow.capture());
        assertEquals(userId, savedRow.getValue().getUserId());
        assertEquals(challengeId, savedRow.getValue().getChallengeId());
        assertEquals(LANGUAGE, savedRow.getValue().getLanguage());
        assertNotNull(savedRow.getValue().getSolvedAt());

        verify(scoreService).updateScoreAndSolvedCounters(userId, 10, 8, DIFFICULTY);
    }

    @Test
    public void givenTheChallengeWasAlreadySolvedInAnotherLanguageWhenTheAcceptedSubmissionIsRecordedThenTheLanguageRowIsSavedButNoScoreIsAwarded() {
        // given the challenge was already solved, but not in this language
        when(solvedChallengesRepository.existsByUserIdAndChallengeId(userId, challengeId)).thenReturn(true);
        when(solvedChallengesRepository.existsByUserIdAndChallengeIdAndLanguage(userId, challengeId, LANGUAGE))
                .thenReturn(false);

        // when the accepted submission is recorded
        solveRecordingService.recordAcceptedSubmission(acceptedSubmission());

        // then the new language row is saved but the score is never awarded twice
        verify(solvedChallengesRepository).save(any(minicode.com.entity.Challange.SolvedChallenges.class));
        verify(scoreService, never()).updateScoreAndSolvedCounters(any(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), any());
    }

    @Test
    public void givenTheChallengeWasAlreadySolvedInTheSameLanguageWhenTheAcceptedSubmissionIsRecordedThenNothingIsSavedAndNoScoreIsAwarded() {
        // given the challenge was already solved in this very language
        when(solvedChallengesRepository.existsByUserIdAndChallengeId(userId, challengeId)).thenReturn(true);
        when(solvedChallengesRepository.existsByUserIdAndChallengeIdAndLanguage(userId, challengeId, LANGUAGE))
                .thenReturn(true);

        // when the accepted submission is recorded
        solveRecordingService.recordAcceptedSubmission(acceptedSubmission());

        // then no duplicate row is saved and no score is awarded
        verify(solvedChallengesRepository, never()).save(any());
        verify(scoreService, never()).updateScoreAndSolvedCounters(any(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), any());
    }

    @Test
    public void givenTheUserRowNoLongerExistsWhenTheAcceptedSubmissionIsRecordedThenTheRecordingIsRejected() {
        // given the user row disappeared between the submission and the recording
        when(userRepo.findByIdForUpdate(userId)).thenReturn(Optional.empty());

        // when the accepted submission is recorded
        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> solveRecordingService.recordAcceptedSubmission(acceptedSubmission())
        );

        // then the recording is rejected and nothing was checked or written
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(solvedChallengesRepository, never()).save(any());
        verify(scoreService, never()).updateScoreAndSolvedCounters(any(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), any());
    }

    private AcceptedSubmission acceptedSubmission() {
        return new AcceptedSubmission(
                userId,
                challengeId,
                DIFFICULTY,
                LANGUAGE,
                "print('solved')",
                8,
                2
        );
    }
}