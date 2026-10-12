package minicode.com.service.Challange;

import minicode.com.dto.Challenge.Request.CreateChallangeRequest;
import minicode.com.dto.Challenge.Request.CreateExamplesRequest;
import minicode.com.dto.Challenge.Request.CreateLanguageSetupRequest;
import minicode.com.dto.Challenge.Request.CreateTestCaseRequest;
import minicode.com.dto.Challenge.Response.ChallengeTemplateResponse;
import minicode.com.entity.Challange.*;
import minicode.com.repository.ChallengeLanguageSetupRepo;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.solvedChallengesRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChallengeService {
    private final ChallengeRepo challengeRepo;
    private final solvedChallengesRepo solvedChallengesRepo;
    private final ChallengeLanguageSetupRepo languageSetupRepo;

    public ChallengeService(
            ChallengeRepo challengeRepo,
            solvedChallengesRepo solvedChallengesRepo,
            ChallengeLanguageSetupRepo languageSetupRepo
    ) {

        this.challengeRepo = challengeRepo;
        this.solvedChallengesRepo = solvedChallengesRepo;
        this.languageSetupRepo = languageSetupRepo;
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
        return getChallengeTemplate(challengeId, null);
    }

    /**
     * Returns the starter template for the requested language. Falls back to
     * the challenge's legacy single-language template when no per-language
     * setup is authored.
     */
    public ChallengeTemplateResponse getChallengeTemplate(UUID challengeId, String language) {
        boolean IsLanguageValid = language != null && !language.isBlank();
        if (IsLanguageValid) {
            Optional<ChallengeLanguageSetup> setup =
                    languageSetupRepo.findByChallenge_IdAndLanguage(challengeId, normalizeLanguage(language));
            if (setup.isPresent()) {
                ChallengeLanguageSetup value = setup.get();
                return new ChallengeTemplateResponse(
                        value.getLanguage(),
                        value.getTemplate(),
                        value.getTemplateExplanation()
                );
            }
        }

        Challenge challenge = getChallenge(challengeId);
        return new ChallengeTemplateResponse(
                challenge.getLanguage(),
                challenge.getTemplete(),
                challenge.getTempleteExplanation()
        );
    }

    /**
     * The runner that wraps a submission for the requested language. Falls
     * back to the challenge's legacy runner when no setup exists, so old
     * challenges keep working.
     */
    public String getRunnerForLanguage(Challenge challenge, String language) {
        boolean hasLanguage = language != null && !language.isBlank();
        if (hasLanguage) {
            Optional<ChallengeLanguageSetup> setup =
                    languageSetupRepo.findByChallenge_IdAndLanguage(challenge.getId(), normalizeLanguage(language));
            if (setup.isPresent()) {
                return setup.get().getRunner();
            }
        }
        return challenge.getRunner();
    }

    private String normalizeLanguage(String language) {
        if (language == null) {
            return null;
        }
        return language.toLowerCase(Locale.ROOT);
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

        Challenge challenge = new Challenge();
        challenge.setName(name);
        challenge.setDescription(description);
        challenge.setDifficulty(difficulty);
        challenge.setCategory(category);
        challenge.setNumberOfTestCases(numberOfTestCases);
        challenge.setSolution(solution);
        challenge.setRunner(runner);
        challenge.setRunnerExplanation(runnerExplanation);
        challenge.setTemplete(template);
        challenge.setTempleteExplanation(templateExplanation);
        challenge.setLanguage(request.getLanguage());

        challenge.setHints(request.getHints());

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

        addLanguageSetups(request, challenge);

        return challengeRepo.save(challenge);
    }

    /**
     * Persists per-language templates/runners. When the request supplies a
     * list, every entry is stored (that is what makes a challenge solvable in
     * several languages). Otherwise the legacy single-language fields become
     * the one setup, so existing callers keep working unchanged.
     */
    private void addLanguageSetups(CreateChallangeRequest request, Challenge challenge) {
        List<CreateLanguageSetupRequest> requestedSetups = request.getLanguageSetups();

        boolean hasRequestedSetups = requestedSetups != null && !requestedSetups.isEmpty();
        if (hasRequestedSetups) {
            for (CreateLanguageSetupRequest setup : requestedSetups) {
                ChallengeLanguageSetup languageSetup = new ChallengeLanguageSetup();
                languageSetup.setChallenge(challenge);
                languageSetup.setLanguage(normalizeLanguage(setup.getLanguage()));
                languageSetup.setTemplate(setup.getTemplate());
                languageSetup.setTemplateExplanation(setup.getTemplateExplanation());
                languageSetup.setRunner(setup.getRunner());
                languageSetup.setRunnerExplanation(setup.getRunnerExplanation());
                challenge.getLanguageSetups().add(languageSetup);
            }
            return;
        }

        ChallengeLanguageSetup languageSetup = new ChallengeLanguageSetup();
        languageSetup.setChallenge(challenge);
        languageSetup.setLanguage(normalizeLanguage(request.getLanguage()));
        languageSetup.setTemplate(request.getTemplate());
        languageSetup.setTemplateExplanation(request.getTemplateExplanation());
        languageSetup.setRunner(request.getRunner());
        languageSetup.setRunnerExplanation(request.getRunnerExplanation());
        challenge.getLanguageSetups().add(languageSetup);
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




