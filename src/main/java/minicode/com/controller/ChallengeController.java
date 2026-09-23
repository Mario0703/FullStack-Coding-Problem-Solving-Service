package minicode.com.controller;


import minicode.com.entity.Challenge;
import minicode.com.repository.ChallengeRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {

    private final ChallengeRepo ChallangeRepo;

    public ChallengeController(ChallengeRepo challengeRepository) {
        this.ChallangeRepo = challengeRepository;
    }

    @GetMapping("/ListChallanges")
    public List<Challenge> listChallanges() {
        return ChallangeRepo.findAll();
    }


}
