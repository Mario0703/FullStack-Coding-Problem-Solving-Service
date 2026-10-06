package minicode.com.controller;


import jakarta.validation.Valid;
import minicode.com.config.SecurityConfig;
import minicode.com.dto.Challenge.CreateChallangeRequest;
import minicode.com.dto.Challenge.ChallengeTemplateResponse;
import minicode.com.dto.Challenge.ChallengeSummaryResponse;
import minicode.com.dto.Challenge.CreateTestCaseRequest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.InputOutput;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Challange.TestCases;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.repository.UserRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.entity.User.User;
import minicode.com.service.Challange.ChallengeService;
import minicode.com.service.User.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import minicode.com.dto.User.PublicSubmissionResponse;
import minicode.com.dto.User.SolverResponse;
import minicode.com.dto.User.UserSummary;
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


    @GetMapping("/ListChallanges")
    public List<ChallengeSummaryResponse> listChallenges(Authentication authentication) {
        checkIfAdmin(authentication);
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
        checkIfAdmin(authentication);
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
        return List.of(
                submissionRepo.findPassedTestCasesByUserIdPublic(userId),
                submissionRepo.findFailedTestCasesByUserIdPublic(userId),
                submissionRepo.findTotalTestCasesByUserIdPublic(userId)
        );
    }

    @GetMapping("Solution/{challengeId}")
    public String getChallengeSolution(@PathVariable UUID challengeId, Authentication authentication) {
        // The instructor solution is an answer, so it must not be readable
        // before the user actually solves the challenge. Admins keep full
        // access; solvers get it once they have an accepted submission.
        if (!isAdmin(authentication)) {
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



    @PostMapping("/create")
    public Challenge createChallenge(@Valid @RequestBody CreateChallangeRequest request, Authentication authentication) {
        checkIfAdmin(authentication);
        Challenge challenge = challangeService.createChallenge(request);
        return challenge;
    }

    private void checkIfAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        if (!isAdmin(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    private UUID requireAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return userRepo.findFirstByUserName(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user was not found"
                ))
                .getUserId();
    }

    @DeleteMapping("/delete")
    public void deleteChallenge(@RequestParam UUID challengeId, Authentication authentication) {
        checkIfAdmin(authentication);
        challangeService.deleteChallenge(challengeId);
    }


}