package minicode.com.service.Challange;

import minicode.com.dto.Challenge.CreateChallangeRequest;
import minicode.com.dto.Challenge.CreateExamplesRequest;
import minicode.com.dto.Challenge.CreateLanguageSetupRequest;
import minicode.com.dto.Challenge.CreateTestCaseRequest;
import minicode.com.dto.Challenge.ChallengeTemplateResponse;
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
        if (language != null && !language.isBlank()) {
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
        if (language != null && !language.isBlank()) {
            Optional<ChallengeLanguageSetup> setup =
                    languageSetupRepo.findByChallenge_IdAndLanguage(challenge.getId(), normalizeLanguage(language));
            if (setup.isPresent()) {
                return setup.get().getRunner();
            }
        }
        return challenge.getRunner();
    }

    private String normalizeLanguage(String language) {
        return language == null ? null : language.toLowerCase(Locale.ROOT);
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

        if (requestedSetups != null && !requestedSetups.isEmpty()) {
            for (CreateLanguageSetupRequest setup : requestedSetups) {
                challenge.getLanguageSetups().add(new ChallengeLanguageSetup(
                        challenge,
                        normalizeLanguage(setup.getLanguage()),
                        setup.getTemplate(),
                        setup.getTemplateExplanation(),
                        setup.getRunner(),
                        setup.getRunnerExplanation()
                ));
            }
            return;
        }

        challenge.getLanguageSetups().add(new ChallengeLanguageSetup(
                challenge,
                normalizeLanguage(request.getLanguage()),
                request.getTemplate(),
                request.getTemplateExplanation(),
                request.getRunner(),
                request.getRunnerExplanation()
        ));
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




