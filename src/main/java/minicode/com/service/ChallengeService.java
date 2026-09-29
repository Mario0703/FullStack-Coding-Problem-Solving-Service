package minicode.com.service;

import minicode.com.dto.CreateChallangeRequest;
import minicode.com.dto.CreateExamplesRequest;
import minicode.com.dto.CreateTestCaseRequest;
import minicode.com.entity.*;
import minicode.com.repository.ChallengeRepo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChallengeService {
    private final ChallengeRepo challengeRepo;

    public ChallengeService(ChallengeRepo challengeRepo) {
        this.challengeRepo = challengeRepo;
    }

    public Iterable<Challenge> listChallenges() {
        return challengeRepo.findAll();
    }

    public Challenge createChallenge(CreateChallangeRequest request) {
        String name = request.getName();
        String description = request.getDescription();
        Difficulty difficulty = request.getDifficulty();
        Category category = request.getCategory();
        int numberOfTestCases = request.getTestCases().size();
        Challenge challenge = new Challenge(
                name,
                description,
                difficulty,
                category,
                numberOfTestCases
        );

        for (CreateExamplesRequest example : request.getExamples()) {
            InputOutput inputOutput = new InputOutput();
            inputOutput.setInputExample(example.getInputExample());
            inputOutput.setOutputExample(example.getOutputExample());
            inputOutput.setChallenge(challenge);
            challenge.getInputOutput().add(inputOutput);
        }

        for (CreateTestCaseRequest testCaseRequest : request.getTestCases()) {
            TestCases testCases = new TestCases();
            testCases.setTestInput(testCaseRequest.getInput());
            testCases.setTestOutput(testCaseRequest.getExpectedOutput());
            testCases.setChallenge(challenge);
            challenge.getTestCases().add(testCases);
        }

        return challengeRepo.save(challenge);
    }

    public void deleteChallenge(UUID challengeid) {
        Challenge challenge = challengeRepo.findById(challengeid)
                .orElseThrow(() -> new RuntimeException("Challenge not found with id: " + challengeid));
        challengeRepo.deleteById(challenge.getId());
    }




}
