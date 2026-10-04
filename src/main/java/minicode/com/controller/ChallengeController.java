package minicode.com.controller;


import jakarta.validation.Valid;
import minicode.com.config.SecurityConfig;
import minicode.com.dto.Challenge.CreateChallangeRequest;
import minicode.com.dto.Challenge.ChallengeTemplateResponse;
import minicode.com.dto.Challenge.CreateTestCaseRequest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.InputOutput;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Challange.TestCases;
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

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challangeService;
    private final UserService userService;
    private final SubmissionRepo submissionRepo;

    public ChallengeController(
            ChallengeService challengeService,
            UserService userService,
            SubmissionRepo submissionRepo
    ) {
        this.challangeService = challengeService;
        this.userService = userService;
        this.submissionRepo = submissionRepo;
    }

    @GetMapping("/list")
    public Iterable<Challenge> listPublicChallenges() {
        return challangeService.listChallenges();
    }


    @GetMapping("/ListChallanges")
    public Iterable<Challenge> listChallenges(Authentication authentication) {
        checkIfAdmin(authentication);
        return challangeService.listChallenges();
    }

    @GetMapping("/{challengeId}/template")
    public ChallengeTemplateResponse getChallengeTemplate(@PathVariable UUID challengeId) {
        return challangeService.getChallengeTemplate(challengeId);
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
    public String getChallengeSolution(@PathVariable UUID challengeId) {
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
        if (authentication.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }

    @DeleteMapping("/delete")
    public void deleteChallenge(@RequestParam UUID challengeId, Authentication authentication) {
        checkIfAdmin(authentication);
        challangeService.deleteChallenge(challengeId);
    }


}