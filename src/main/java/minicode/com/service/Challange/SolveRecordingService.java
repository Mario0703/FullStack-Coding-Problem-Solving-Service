package minicode.com.service.Challange;

import jakarta.transaction.Transactional;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import minicode.com.service.scoreService.ScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

/**
 * Records an accepted submission in one transaction: the solved row (once
 * per language) and the score award (once per challenge). The user row is
 * locked for the whole transaction, so two accepted submissions of the same
 * user serialize behind that lock and the exists-checks can never race —
 * the second one only runs after the first one committed.
 */
@Service
public class SolveRecordingService {

    private final UserRepo userRepo;
    private final solvedChallengesRepo solvedChallengesRepository;
    private final ScoreService scoreService;

    public SolveRecordingService(
            UserRepo userRepo,
            solvedChallengesRepo solvedChallengesRepository,
            ScoreService scoreService
    ) {
        this.userRepo = userRepo;
        this.solvedChallengesRepository = solvedChallengesRepository;
        this.scoreService = scoreService;
    }

    @Transactional
    public void recordAcceptedSubmission(AcceptedSubmission submission) {
        UUID userId = submission.userId();
        UUID challengeId = submission.challengeId();

        userRepo.findByIdForUpdate(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        boolean alreadySolvedChallenge =
                solvedChallengesRepository.existsByUserIdAndChallengeId(userId, challengeId);
        boolean alreadySolvedInLanguage = solvedChallengesRepository
                .existsByUserIdAndChallengeIdAndLanguage(userId, challengeId, submission.language());

        if (!alreadySolvedInLanguage) {
            solvedChallengesRepository.save(solvedRowFor(submission));
        }

        if (alreadySolvedChallenge) {
            return;
        }

        scoreService.updateScoreAndSolvedCounters(
                userId,
                submission.totalTestCases(),
                submission.passingTestCases(),
                submission.difficulty()
        );
    }

    private SolvedChallenges solvedRowFor(AcceptedSubmission submission) {
        SolvedChallenges solved = new SolvedChallenges();
        solved.setUserId(submission.userId());
        solved.setChallengeId(submission.challengeId());
        solved.setCode(submission.code());
        solved.setLanguage(submission.language());
        solved.setSolvedAt(Instant.now());
        return solved;
    }
}