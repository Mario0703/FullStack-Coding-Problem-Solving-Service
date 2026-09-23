package minicode.com.controller;


import minicode.com.dto.CreateChallangeRequest;
import minicode.com.entity.Challenge;
import minicode.com.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challangeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challangeService = challengeService;
    }

    @GetMapping("/ListChallanges")
    public Iterable<Challenge> listChallenges() {
        return challangeService.listChallenges();
    }

    @PostMapping("/create")
    public Challenge createChallenge(@Valid @RequestBody CreateChallangeRequest request) {
        return challangeService.createChallenge(request);
    }


}
