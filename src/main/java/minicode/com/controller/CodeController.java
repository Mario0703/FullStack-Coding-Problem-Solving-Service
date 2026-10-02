package minicode.com.controller;

import minicode.com.controller.CoderExercutes.DockerCodeStrategy;
import minicode.com.dto.User.ExecutionResult;
import minicode.com.dto.User.SubmissionRequest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.TestCases;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.Container.Container;
import minicode.com.entity.TestRunner.TestRunner;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.ContainerRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.TestRunnerRepo;
import minicode.com.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/code/playground")
public class CodeController {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of(
            "python", "java", "c++", "c#", "csharp"
    );
    private final DockerCodeStrategy strategy = new DockerCodeStrategy();
    private final ChallengeRepo challengeRepo;
    private final UserRepo userRepo;
    private final SubmissionRepo submissionRepo;
    private final ContainerRepo containerRepo;
    private final TestRunnerRepo testRunnerRepo;

    public CodeController(
            ChallengeRepo challengeRepo,
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            ContainerRepo containerRepo,
            TestRunnerRepo testRunnerRepo
    ) {
        this.challengeRepo = challengeRepo;
        this.userRepo = userRepo;
        this.submissionRepo = submissionRepo;
        this.containerRepo = containerRepo;
        this.testRunnerRepo = testRunnerRepo;
    }

    @PostMapping("/submit")
    public ExecutionResult submitCode(
            @Valid @RequestBody SubmissionRequest codeSubmission,
            Authentication authentication
    ) throws IOException {
        UUID authenticatedUserId = getAuthenticatedUserId(authentication);
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
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported language: " + language);
        }
        Path languagePath = Path.of("CodeEnv", language);
        Path tempEnvPath = null;

        try {
            tempEnvPath = prepareTemporaryEnvForSubmissionTest(
                    languagePath,
                    codeSubmission.getCode(),
                    challenge.getRunner(),
                    language,
                    challenge.getTestCases()
            );

            ExecutionResult result = strategy.execute(tempEnvPath);
            SubmissionStatus status = result.exitCode() == 0
                    ? SubmissionStatus.ACCEPTED
                    : SubmissionStatus.WRONG_ANSWER;

            submissionRepo.save(new Submission(
                    user,
                    challenge,
                    codeSubmission.getCode(),
                    language,
                    result.exitCode(),
                    status,
                    result.passingTestCases(),
                    result.failingTestCases()
            ));


            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Code execution was interrupted", e);
        } finally {
            if (tempEnvPath != null) {
                try {
                    deleteDirectory(tempEnvPath);
                } catch (IOException e) {
                    // Do not hide the submission result if cleanup fails.
                    e.printStackTrace();
                }
            }
        }
    }
    @GetMapping("/SolvedChallenges")
    public List<UUID> getSolvedChallenges(Authentication authentication) {
        UUID userId = getAuthenticatedUserId(authentication);
        return submissionRepo.findChallengeIdsByUserIdAndStatus(
                userId,
                SubmissionStatus.ACCEPTED
        );
    }

    private Path prepareTemporaryEnvForSubmissionTest(
            Path languagePath,
            String submissionCode,
            String runnerCode,
            String language,
            List<TestCases> testCases
    ) {

        try {
            Files.createDirectories(languagePath);
            Path tempEnv = Files.createTempDirectory(languagePath, "tempEnv-");

            String metadataLanguage = language.equals("csharp") ? "c#" : language;
            Container container = containerRepo.findById(metadataLanguage).orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Container metadata was not found for " + language
                    ));
            TestRunner testRunner = testRunnerRepo.findById(metadataLanguage).orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Test runner metadata was not found for " + language
                    ));

            Files.writeString(tempEnv.resolve("Dockerfile"), container.getContainerMetadata());
            Path testSuiteDirectory = tempEnv.resolve("TestSuite");
            Files.createDirectories(testSuiteDirectory);
            Files.writeString(
                    testSuiteDirectory.resolve("runCommandTest.bs"),
                    testRunner.getTestRunnerMetaData()
            );

            String submissionFileName = switch (language) {
                case "python" -> "submission.py";
                case "c++" -> "submission.cpp";
                case "c#", "csharp" -> "submission.cs";
                case "java" -> "submission.java";
                default -> throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Unsupported language: " + language
                );
            };
            Path submissionFile = tempEnv.resolve(submissionFileName);

            String executableCode = switch (language) {
                case "java", "c#", "csharp" ->
                        Objects.requireNonNullElse(runnerCode, "") + "\n" + submissionCode;
                default ->
                        submissionCode + "\n" + Objects.requireNonNullElse(runnerCode, "");
            };
            Files.writeString(submissionFile, executableCode, StandardCharsets.UTF_8);

            // Create test cases files
            for (int i = 0; i < testCases.size(); i++) {
                TestCases testCase = testCases.get(i);
                Path testCasesDirectory = tempEnv.resolve("TestCases");
                Files.createDirectories(testCasesDirectory);
                Path inputFile = testCasesDirectory.resolve("test-case" + (i + 1) + ".in");
                Path outputFile = testCasesDirectory.resolve("test-case" + (i + 1) + ".out");
                Files.writeString(
                        inputFile,
                        Objects.requireNonNullElse(testCase.getTestInput(), ""),
                        StandardCharsets.UTF_8
                );
                Files.writeString(
                        outputFile,
                        Objects.requireNonNullElse(testCase.getTestOutput(), ""),
                        StandardCharsets.UTF_8
                );
            }

            return tempEnv;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary environment", e);
        }
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            try (Stream<Path> paths = Files.walk(path)) {
                paths.sorted(Comparator.reverseOrder())
                        .forEach(currentPath -> {
                            try {
                                Files.deleteIfExists(currentPath);
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            } catch (UncheckedIOException e) {
                throw e.getCause();
            }
        }
    }


    private UUID getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        User user = userRepo.findFirstByUserName(authentication.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated"));
        if (user.getRole() == UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User access is not permitted for an admin session");
        }

        return user.getUserId();
    }
}



