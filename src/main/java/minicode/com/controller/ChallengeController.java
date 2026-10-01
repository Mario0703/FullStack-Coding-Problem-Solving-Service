package minicode.com.controller;


import jakarta.servlet.http.HttpSession;
import minicode.com.dto.CreateChallangeRequest;
import minicode.com.dto.ChallengeTemplateResponse;
import minicode.com.dto.CreateTestCaseRequest;
import minicode.com.entity.Challenge;
import minicode.com.entity.User;
import minicode.com.entity.UserRole;
import minicode.com.repository.UserRepo;
import minicode.com.service.ChallengeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challangeService;
    private final UserRepo userRepo;

    public ChallengeController(ChallengeService challengeService, UserRepo userRepo) {
        this.challangeService = challengeService;
        this.userRepo = userRepo;
    }

    @GetMapping("/list")
    public Iterable<Challenge> listPublicChallenges() {
        return challangeService.listChallenges();
    }


    @GetMapping("/ListChallanges")
    public Iterable<Challenge> listChallenges(HttpSession session) {
        checkIfAdmin(session);
        return challangeService.listChallenges();
    }

    @GetMapping("/{challengeId}/template")
    public ChallengeTemplateResponse getChallengeTemplate(@PathVariable UUID challengeId) {
        File templateFolder = new File("CodeEnv/Challenges/" + challengeId + "/Template");
        File[] templateFiles = templateFolder.listFiles((directory, fileName) ->
                fileName.startsWith("template.") && !fileName.equals("template-explanation.txt"));

        if (templateFiles == null || templateFiles.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge template not found");
        }

        File templateFile = templateFiles[0];
        String fileName = templateFile.getName();
        String language = fileName.substring("template.".length());
        File explanationFile = new File(templateFolder, "template-explanation.txt");

        try {
            String template = Files.readString(templateFile.toPath(), StandardCharsets.UTF_8);
            String explanation = explanationFile.exists()
                    ? Files.readString(explanationFile.toPath(), StandardCharsets.UTF_8)
                    : "";
            return new ChallengeTemplateResponse(language, template, explanation);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not read challenge template",
                    exception
            );
        }
    }

    @GetMapping("/{challengeId}/examples")
    public List<minicode.com.entity.InputOutput> getChallengeExamples(
            @PathVariable UUID challengeId,
            HttpSession session
    ) {
        checkIfAdmin(session);
        return challangeService.getChallengeExamples(challengeId);
    }

    @GetMapping("/{challengeId}/test-cases")
    public List<minicode.com.entity.TestCases> getChallengeTestCases(
            @PathVariable UUID challengeId,
            HttpSession session
    ) {
        checkIfAdmin(session);
        return challangeService.getChallengeTestCases(challengeId);
    }

    @PostMapping("/create")
    public Challenge createChallenge( @RequestBody CreateChallangeRequest request, HttpSession session) {
        checkIfAdmin(session);
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

            File templateFolder = new File(challengeFolder, "Template");
            if (!templateFolder.exists()) {
                templateFolder.mkdirs();
            }
            File templateFile = new File(templateFolder, "template." + request.getLanguage());
            try (FileWriter writer = new FileWriter(templateFile)) {
                writer.write(request.getTemplate());
            }
            try (FileWriter writer = new FileWriter(new File(templateFolder, "template-explanation.txt"))) {
                writer.write(request.getTemplateExplanation());
            }

            File runnerFolder = new File(challengeFolder, "Runner");
            if (!runnerFolder.exists()) {
                runnerFolder.mkdirs();
            }
            File runnerFile = new File(runnerFolder, "runner." + request.getLanguage());
            try (FileWriter writer = new FileWriter(runnerFile)) {
                writer.write(request.getRunner());
            }
            try (FileWriter writer = new FileWriter(new File(runnerFolder, "runner-explanation.txt"))) {
                writer.write(request.getRunnerExplanation());
            }

            for (int i = 0; i < request.getTestCases().size(); i++) {
                CreateTestCaseRequest testCase = request.getTestCases().get(i);
                File inputFile = new File(testCasesFolder, "test-case" + (i + 1) + ".in");
                File outputFile = new File(testCasesFolder, "test-case" + (i + 1) + ".out");
                try (FileWriter writer = new FileWriter(inputFile)) {
                    writer.write(testCase.getInput());
                }
                try (FileWriter writer = new FileWriter(outputFile)) {
                    writer.write(testCase.getExpectedOutput());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return challenge;
    }

    private void checkIfAdmin(HttpSession session) {
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        Object userId = session.getAttribute("user_id");
        if (!(userId instanceof UUID)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        User user = userRepo.findById((UUID) userId).orElse(null);
        if (user == null || user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin is not authenticated");
        }
    }

    @DeleteMapping("/delete")
    public void deleteChallenge(@RequestParam UUID challengeId, HttpSession session) {
        checkIfAdmin(session);
        challangeService.deleteChallenge(challengeId);
    }


}
