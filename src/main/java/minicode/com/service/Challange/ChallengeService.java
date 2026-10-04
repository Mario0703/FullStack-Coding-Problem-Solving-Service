package minicode.com.service.Challange;

import minicode.com.dto.Challenge.CreateChallangeRequest;
import minicode.com.dto.Challenge.CreateExamplesRequest;
import minicode.com.dto.Challenge.CreateTestCaseRequest;
import minicode.com.dto.Challenge.ChallengeTemplateResponse;
import minicode.com.entity.Challange.*;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.solvedChallengesRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {
    private final ChallengeRepo challengeRepo;
    private final solvedChallengesRepo solvedChallengesRepo;

    public ChallengeService(ChallengeRepo challengeRepo, solvedChallengesRepo solvedChallengesRepo) {

        this.challengeRepo = challengeRepo;
        this.solvedChallengesRepo = solvedChallengesRepo;
    }

    public Iterable<Challenge> listChallenges() {
        return challengeRepo.findAll();
    }

    public List<InputOutput> getChallengeExamples(UUID challengeId) {
        Challenge challenge = getChallenge(challengeId);
        return challenge.getInputOutput();
    }

    public List<TestCases> getChallengeTestCases(UUID challengeId) {
        Challenge challenge = getChallenge(challengeId);
        return challenge.getTestCases();
    }

    public ChallengeTemplateResponse getChallengeTemplate(UUID challengeId) {
        Challenge challenge = getChallenge(challengeId);
        return new ChallengeTemplateResponse(
                challenge.getLanguage(),
                challenge.getTemplete(),
                challenge.getTempleteExplanation()
        );
    }

    public Challenge createChallenge(CreateChallangeRequest request) {

        String name = request.getName();

        String description = request.getDescription();
        Difficulty difficulty = request.getDifficulty();
        Category category = request.getCategory();
        int numberOfTestCases = request.getTestCases().size();
        String solution = request.getSolution();
        String template = request.getTemplate();
        String templateExplanation = request.getTemplateExplanation();
        String runner = request.getRunner();
        String runnerExplanation = request.getRunnerExplanation();

        Challenge challenge = new Challenge(
                name,
                description,
                difficulty,
                category,
                numberOfTestCases,
                solution,
                runner,
                runnerExplanation,
                template,
                templateExplanation,
                request.getLanguage()
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

    public List<SolvedChallenges> getSolvedChallenges(UUID challengeId) {
        return solvedChallengesRepo.findAllByChallengeId(challengeId);
    }

    public Challenge getChallenge(UUID challengeId) {
        return challengeRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found with id: " + challengeId));
    }




}




