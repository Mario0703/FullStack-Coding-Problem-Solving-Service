package minicode.com.controller;


import minicode.com.dto.CreateChallangeRequest;
import minicode.com.entity.Challenge;
import minicode.com.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

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
        Challenge challenge = challangeService.createChallenge(request);
        UUID challengeId = challenge.getId();
        File challengeFolder = new File("CodeEnv/Challenges/" + challengeId);

        File solutionFolder = new File(challengeFolder, "Solution");
        File testCasesFolder = new File(challengeFolder, "TestCases");

        try {
            if (!challengeFolder.exists()) {
                challengeFolder.mkdirs();
            }
            if (!solutionFolder.exists()) {
                solutionFolder.mkdirs();
            }
            if (!testCasesFolder.exists()) {
                testCasesFolder.mkdirs();
            }

            File solutionFile = new File(solutionFolder, "solution." + request.getLanguage());
            try (FileWriter writer = new FileWriter(solutionFile)) {
                writer.write(request.getSolution());
            }

            for (int i = 0; i < request.getTestCases().size(); i++) {
                String testCase = request.getTestCases().get(i);
                File testCaseFile = new File(testCasesFolder, "test" + (i + 1) + ".txt");
                try (FileWriter writer = new FileWriter(testCaseFile)) {
                    writer.write(testCase);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return challenge;
    }


}
