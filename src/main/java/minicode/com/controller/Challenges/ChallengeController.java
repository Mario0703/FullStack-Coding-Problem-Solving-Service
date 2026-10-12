package minicode.com.controller.Challenges;


import jakarta.validation.Valid;
import minicode.com.auth.Access;
import minicode.com.dto.Challenge.Request.CreateChallangeRequest;
import minicode.com.dto.Challenge.Response.ChallengeAiContextResponse;
import minicode.com.dto.Challenge.Response.ChallengeTemplateResponse;
import minicode.com.dto.Challenge.Response.ChallengeSummaryResponse;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.InputOutput;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Challange.TestCases;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.exception.UnauthenticatedException;
import minicode.com.repository.UserRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.service.Challange.ChallengeService;
import minicode.com.service.User.UserService;
import org.springframework.http.HttpStatus;
import minicode.com.dto.User.Response.PublicSubmissionResponse;
import minicode.com.dto.User.Response.SolverResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challangeService;
    private final UserService userService;
    private final SubmissionRepo submissionRepo;
    private final UserRepo userRepo;

    public ChallengeController(
            ChallengeService challengeService,
            UserService userService,
            SubmissionRepo submissionRepo,
            UserRepo userRepo
    ) {
        this.challangeService = challengeService;
        this.userService = userService;
        this.submissionRepo = submissionRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/list")
    public List<ChallengeSummaryResponse> listPublicChallenges() {
        return toSummaries(challangeService.listChallenges());
    }

    private List<ChallengeSummaryResponse> toSummaries(Iterable<Challenge> challenges) {
        List<ChallengeSummaryResponse> summaries = new ArrayList<>();
        for (Challenge challenge : challenges) {
            summaries.add(ChallengeSummaryResponse.from(challenge));
        }
        return summaries;
    }

    @GetMapping("/{challengeId}/template")
    public ChallengeTemplateResponse getChallengeTemplate(
            @PathVariable UUID challengeId,
            @RequestParam(required = false) String language
    ) {
        return challangeService.getChallengeTemplate(challengeId, language);
    }

    // Examples are part of the problem statement, so any solver can read them
    // (like the template), unlike the hidden test cases below.
    @GetMapping("/{challengeId}/examples")
    public List<InputOutput> getChallengeExamples(@PathVariable UUID challengeId) {
        return challangeService.getChallengeExamples(challengeId);
    }

    @GetMapping("/{challengeId}/test-cases")
    public List<TestCases> getChallengeTestCases(
            @PathVariable UUID challengeId,
            Authentication authentication
    ) {
        Access.requireAdmin(authentication);
        return challangeService.getChallengeTestCases(challengeId);
    }
    @GetMapping("/{challengeId}/getSolvedChallenges")
    public List<SolvedChallenges> getSolvedChallenges(@PathVariable UUID challengeId) {
        return challangeService.getSolvedChallenges(challengeId);
    }

    @GetMapping("/{challengeId}/solved-by")
    public List<SolverResponse> getSolvedByUserNames(@PathVariable UUID challengeId) {
        List<SolvedChallenges> solvedChallenges = challangeService.getSolvedChallenges(challengeId);
        return userService.getSolversBySolvedChallenges(solvedChallenges);
    }

    @GetMapping("/{challengeId}/submissions")
    public List<PublicSubmissionResponse> getAcceptedSubmissionsForChallenge(@PathVariable UUID challengeId) {
        return submissionRepo.findAcceptedByChallengeId(challengeId).stream()
                .map(PublicSubmissionResponse::from)
                .toList();
    }



    @GetMapping("/users/{userId}/submissions")
    public List<PublicSubmissionResponse> getUserSubmissions(@PathVariable UUID userId) {
        return submissionRepo.findPublicSubmissionsByUserId(userId).stream()
                .map(PublicSubmissionResponse::from)
                .toList();
    }

    @GetMapping("/users/{userId}/submission-stats")
    public List<List<Integer>> getUserSubmissionStats(@PathVariable UUID userId) {
        return SubmissionHelper.submissionStats(submissionRepo, userId);
    }

    @GetMapping("Solution/{challengeId}")
    public String getChallengeSolution(@PathVariable UUID challengeId, Authentication authentication) {
        // The instructor solution is an answer, so it must not be readable
        // before the user actually solves the challenge. Admins keep full
        // access; solvers get it once they have an accepted submission.
        if (!Access.isAdmin(authentication)) {
            UUID userId = requireAuthenticatedUserId(authentication);
            boolean solved = submissionRepo.existsByUser_UserIdAndChallenge_IdAndStatus(
                    userId,
                    challengeId,
                    SubmissionStatus.ACCEPTED
            );
            if (!solved) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Solve the challenge before viewing the instructor solution"
                );
            }
        }
        Challenge challenge = challangeService.getChallenge(challengeId);
        return challenge.getSolution();
    }

    /**
     * Single, authenticated feed for the MiniCode AI helper: the challenge
     * description, examples, test cases, hints, the caller's own latest code
     * and, once allowed, the instructor solution.
     */
    @GetMapping("/{challengeId}/ai-context")
    public ChallengeAiContextResponse getChallengeAiContext(
            @PathVariable UUID challengeId,
            Authentication authentication
    ) {
        UUID userId = requireAuthenticatedUserId(authentication);
        Challenge challenge = challangeService.getChallenge(challengeId);

        boolean admin = Access.isAdmin(authentication);
        boolean solved = submissionRepo.existsByUser_UserIdAndChallenge_IdAndStatus(
                userId,
                challengeId,
                SubmissionStatus.ACCEPTED
        );

        // Mirror /Solution/{id}: the instructor solution stays hidden until the
        // user has solved the challenge (admins keep full access).
        boolean solutionAvailable = admin || solved;
        String solution = solutionAvailable ? challenge.getSolution() : null;

        Submission latestSubmission = submissionRepo
                .findFirstByUser_UserIdAndChallenge_IdOrderByCreatedAtDesc(userId, challengeId)
                .orElse(null);

        return ChallengeAiContextResponse.from(
                challenge,
                splitHints(challenge.getHints()),
                solution,
                latestSubmission,
                solved
        );
    }

    private List<String> splitHints(String hints) {
        if (hints == null || hints.isBlank()) {
            return List.of();
        }
        List<String> parsed = new ArrayList<>();
        for (String line : hints.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                parsed.add(trimmed);
            }
        }
        return parsed;
    }



    @PostMapping("/create")
    public Challenge createChallenge(@Valid @RequestBody CreateChallangeRequest request, Authentication authentication) {
        Access.requireAdmin(authentication);
        Challenge challenge = challangeService.createChallenge(request);
        return challenge;
    }

    private UUID requireAuthenticatedUserId(Authentication authentication) {
        Access.requireAuthenticated(authentication);
        return userRepo.findFirstByUserName(authentication.getName())
                .orElseThrow(UnauthenticatedException::authenticationRequired)
                .getUserId();
    }

    @DeleteMapping("/delete")
    public void deleteChallenge(@RequestParam UUID challengeId, Authentication authentication) {
        Access.requireAdmin(authentication);
        challangeService.deleteChallenge(challengeId);
    }


}