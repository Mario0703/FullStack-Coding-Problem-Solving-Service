package minicode.com.controller;

import minicode.com.controller.CoderExercutes.CodeExercuteStrat;
import minicode.com.controller.CoderExercutes.CSharpStrat;
import minicode.com.controller.CoderExercutes.CppStrat;
import minicode.com.controller.CoderExercutes.JavaStrat;
import minicode.com.controller.CoderExercutes.PythonStrat;
import minicode.com.dto.ExecutionResult;
import minicode.com.dto.SubmissionRequest;
import minicode.com.entity.Challenge;
import minicode.com.entity.Submission;
import minicode.com.entity.SubmissionStatus;
import minicode.com.entity.User;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/code/playground")
public class CodeController {

    private final HashMap<String, CodeExercuteStrat> strategies = new HashMap<>();
    private final ChallengeRepo challengeRepo;
    private final UserRepo userRepo;
    private final SubmissionRepo submissionRepo;

    public CodeController(
            ChallengeRepo challengeRepo,
            UserRepo userRepo,
            SubmissionRepo submissionRepo
    ) {
        this.challengeRepo = challengeRepo;
        this.userRepo = userRepo;
        this.submissionRepo = submissionRepo;
        strategies.put("python", new PythonStrat());
        strategies.put("java", new JavaStrat());
        strategies.put("cpp", new CppStrat());
        strategies.put("c++", new CppStrat());
        strategies.put("csharp", new CSharpStrat());
        strategies.put("c#", new CSharpStrat());
    }

    @PostMapping("/submit")
    public ExecutionResult submitCode(
            @Valid @RequestBody SubmissionRequest codeSubmission,
            HttpServletRequest httpRequest
    ) throws IOException {
        UUID authenticatedUserId = getAuthenticatedUserId(httpRequest);
        if (!authenticatedUserId.equals(codeSubmission.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User ID does not match the active session");
        }

        User user = userRepo.findById(authenticatedUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user was not found"
                ));

        Challenge challenge = challengeRepo.findById(codeSubmission.getChallengeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Challenge not found"
                ));

        String language = codeSubmission.getLanguage().toLowerCase(Locale.ROOT);
        CodeExercuteStrat strategy = strategies.get(language);

        if (strategy == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported language: " + language);
        }

        Path challengeFolder = Path.of("CodeEnv", "Challenges", codeSubmission.getChallengeId().toString());
        Path submissionFile = saveSubmission(codeSubmission, challenge);

        try {
            ExecutionResult result = strategy.execute(submissionFile, challengeFolder);
            SubmissionStatus status = result.exitCode() == 0
                    ? SubmissionStatus.ACCEPTED
                    : SubmissionStatus.WRONG_ANSWER;

            submissionRepo.save(new Submission(
                    user,
                    challenge,
                    codeSubmission.getCode(),
                    language,
                    result.exitCode(),
                    status
            ));

            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Code execution was interrupted", e);
        }
    }

    private Path saveSubmission(SubmissionRequest submission, Challenge challenge) throws IOException {
        String challengeFolderName = sanitizeFolderName(challenge.getName());
        Path submissionFolder = Path.of(
                "submissions",
                submission.getUserId().toString(),
                challengeFolderName
        );
        Files.createDirectories(submissionFolder);

        Path submissionFile = submissionFolder.resolve(
                "submission." + fileExtension(submission.getLanguage())
        );
        Files.writeString(submissionFile, submission.getCode(), StandardCharsets.UTF_8);
        return submissionFile;
    }

    private String sanitizeFolderName(String name) {
        String sanitized = name
                .trim()
                .replaceAll("[^a-zA-Z0-9._-]", "_")
                .replaceAll("_+", "_");
        return sanitized.isBlank() ? "challenge" : sanitized;
    }

    private String fileExtension(String language) {
        return switch (language.toLowerCase(Locale.ROOT)) {
            case "python" -> "py";
            case "java" -> "java";
            case "cpp", "c++" -> "cpp";
            case "csharp", "c#" -> "cs";
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported language: " + language
            );
        };
    }

    private UUID getAuthenticatedUserId(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Object userId = session.getAttribute("user_id");
        if (!(userId instanceof UUID)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        return (UUID) userId;
    }
}
