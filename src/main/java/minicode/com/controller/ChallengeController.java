package minicode.com.controller;


import minicode.com.entity.Challenge;
import minicode.com.service.ChallengeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
