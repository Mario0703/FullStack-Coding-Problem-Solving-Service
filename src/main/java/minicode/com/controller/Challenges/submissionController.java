package minicode.com.controller.Challenges;

import minicode.com.auth.CurrentUser;
import minicode.com.dto.User.Response.SolvedChallengeResponse;
import minicode.com.dto.User.Response.SubmissionResponse;
import minicode.com.entity.Submissions.Submission;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/submission")
public class submissionController {
    private final SubmissionRepo SubmissionRepo;
    private final UserRepo userRepo;
    private final solvedChallengesRepo solvedChallengesRepo;
    private final ChallengeRepo challengeRepo;

    public submissionController(
            SubmissionRepo submissionRepo,
            UserRepo userRepo,
            solvedChallengesRepo solvedChallengesRepo,
            ChallengeRepo challengeRepo
    ) {
        this.SubmissionRepo = submissionRepo;
        this.userRepo = userRepo;
        this.solvedChallengesRepo = solvedChallengesRepo;
        this.challengeRepo = challengeRepo;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<SubmissionResponse> listUserSubmissions(Authentication authentication) {
        UUID userId = CurrentUser.id(userRepo, authentication);
        return SubmissionRepo.findReceiptByUserId(userId).stream()
                .map(SubmissionResponse::from)
                .toList();
    }

    @GetMapping("/solved")
    @Transactional(readOnly = true)
    public List<SolvedChallengeResponse> listSolvedChallenges(Authentication authentication) {
        UUID userId = CurrentUser.id(userRepo, authentication);
        return SolvedChallengeHelper.buildSolvedChallengeResponses(
                challengeRepo,
                solvedChallengesRepo.findAllByUserId(userId)
        );
    }

    @GetMapping("/details")
    @Transactional(readOnly = true)
    public SubmissionResponse getDetailsFromSubmissionId(
            Authentication authentication,
            @RequestParam UUID submissionId
    ) {
        UUID userId = CurrentUser.id(userRepo, authentication);

        Submission submission = SubmissionRepo.findByIdAndUser_UserId(submissionId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Submission not found"
                ));

        return SubmissionResponse.from(submission);
    }

    @GetMapping("/passed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfPassedTestCases(Authentication authentication) {
        return SubmissionHelper.passedTestCasesForUser(userRepo, SubmissionRepo, authentication);
    }

    @GetMapping("/failed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfFailedTestCases(Authentication authentication) {
        return SubmissionHelper.failedTestCasesForUser(userRepo, SubmissionRepo, authentication);
    }

    @GetMapping("/total")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfTotalTestCases(Authentication authentication) {
        return SubmissionHelper.totalTestCasesForUser(userRepo, SubmissionRepo, authentication);
    }
}
