package minicode.com.controller;

import minicode.com.controller.CoderExercutes.CodeExercuteStrat;
import minicode.com.controller.CoderExercutes.CSharpStrat;
import minicode.com.controller.CoderExercutes.CppStrat;
import minicode.com.controller.CoderExercutes.JavaStrat;
import minicode.com.controller.CoderExercutes.PythonStrat;
import minicode.com.dto.CodeSubmission;
import minicode.com.entity.Challenge;
import minicode.com.repository.ChallengeRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    public CodeController(ChallengeRepo challengeRepo) {
        this.challengeRepo = challengeRepo;
        strategies.put("python", new PythonStrat());
        strategies.put("java", new JavaStrat());
        strategies.put("cpp", new CppStrat());
        strategies.put("c++", new CppStrat());
        strategies.put("csharp", new CSharpStrat());
        strategies.put("c#", new CSharpStrat());
    }

    @PostMapping("/submit")
    public String submitCode(
            @RequestBody CodeSubmission submission,
            HttpServletRequest httpRequest
    ) throws IOException {
        if (submission.userId() == null
                || submission.challengeId() == null
                || submission.language() == null
                || submission.code() == null
                || submission.code().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incomplete code submission");
        }

        UUID authenticatedUserId = getAuthenticatedUserId(httpRequest);
        if (!authenticatedUserId.equals(submission.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User ID does not match the active session");
        }

        Challenge challenge = challengeRepo.findById(submission.challengeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Challenge not found"
                ));

        saveSubmission(submission, challenge);

        String language = submission.language().toLowerCase(Locale.ROOT);
        CodeExercuteStrat strategy = strategies.get(language);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }

        try {
            return strategy.execute(submission.code());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Code execution was interrupted", e);
        }
    }

    private void saveSubmission(CodeSubmission submission, Challenge challenge) throws IOException {
        String challengeFolderName = sanitizeFolderName(challenge.getName());
        Path submissionFolder = Path.of(
                "submissions",
                submission.userId().toString(),
                challengeFolderName
        );
        Files.createDirectories(submissionFolder);

        Path submissionFile = submissionFolder.resolve(
                "submission." + fileExtension(submission.language())
        );
        Files.writeString(submissionFile, submission.code(), StandardCharsets.UTF_8);
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
