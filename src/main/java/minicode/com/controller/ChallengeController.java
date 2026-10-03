package minicode.com.controller;


import jakarta.validation.Valid;
import minicode.com.dto.Challenge.CreateChallangeRequest;
import minicode.com.dto.Challenge.ChallengeTemplateResponse;
import minicode.com.dto.Challenge.CreateTestCaseRequest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.InputOutput;
import minicode.com.entity.Challange.TestCases;
import minicode.com.service.Challange.ChallengeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challangeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challangeService = challengeService;
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

    @GetMapping("/{challengeId}/examples")
    public List<InputOutput> getChallengeExamples(
            @PathVariable UUID challengeId,
            Authentication authentication
    ) {
        checkIfAdmin(authentication);
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



