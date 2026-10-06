package minicode.com.controller.CodeEnv;

import minicode.com.controller.CodeEnv.Submissionfile.SubmissionFileFactory;
import minicode.com.controller.CodeEnv.Submissionfile.SubmissionFileName;
import minicode.com.entity.Challange.TestCases;
import minicode.com.entity.Container.Container;
import minicode.com.entity.TestRunner.TestRunner;
import minicode.com.exception.ContainerFailureException;
import minicode.com.repository.ContainerRepo;
import minicode.com.repository.TestRunnerRepo;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TempEnvDestroyAfter implements EnvStrat {

    private final String language;
    private final Path languagePath;
    private final String submissionCode;
    private final String runnerCode;
    private final List<TestCases> testCases;
    private final ContainerRepo containerRepo;
    private final TestRunnerRepo testRunnerRepo;

    public TempEnvDestroyAfter(
            String language,
            String submissionCode,
            String runnerCode,
            List<TestCases> testCases,
            ContainerRepo containerRepo,
            TestRunnerRepo testRunnerRepo
    ) {
        this.language = language;
        this.languagePath = Path.of("CodeEnv", language);
        this.submissionCode = submissionCode;
        this.runnerCode = runnerCode;
        this.testCases = testCases;
        this.containerRepo = containerRepo;
        this.testRunnerRepo = testRunnerRepo;
    }

    @Override
    public Path createEnv() {
        try {
            Files.createDirectories(languagePath);
            Path tempEnv = Files.createTempDirectory(languagePath, "tempEnv-");

            Container container = resolveContainer(language);
            TestRunner testRunner = resolveTestRunner(language);

            Files.writeString(tempEnv.resolve("Dockerfile"), container.getContainerMetadata());
            Path testSuiteDirectory = tempEnv.resolve("TestSuite");
            Files.createDirectories(testSuiteDirectory);
            Files.writeString(
                    testSuiteDirectory.resolve("runCommandTest.bs"),
                    testRunner.getTestRunnerMetaData()
            );

            SubmissionFileName submissionFileStrategy = SubmissionFileFactory.forLanguage(language);
            Path submissionFile = tempEnv.resolve(submissionFileStrategy.fileName());
            String executableCode = submissionFileStrategy.combineCode(runnerCode, submissionCode);
            Files.writeString(submissionFile, executableCode, StandardCharsets.UTF_8);

            Path testCasesDirectory = tempEnv.resolve("TestCases");
            Files.createDirectories(testCasesDirectory);
            for (int i = 0; i < testCases.size(); i++) {
                TestCases testCase = testCases.get(i);
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
            throw ContainerFailureException.failedToCreateContainer(
                    "Failed to create temporary environment: " + e.getMessage()
            );
        }
    }

    @Override
    public void destroyEnv(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }
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

    private Container resolveContainer(String language) {
        return containerRepo.findById(language).orElseThrow(() ->
                ContainerFailureException.containerFailed(
                        "Container metadata was not found for " + language
                ));
    }

    private TestRunner resolveTestRunner(String language) {
        return testRunnerRepo.findById(language).orElseThrow(() ->
                ContainerFailureException.containerFailed(
                        "Test runner metadata was not found for " + language
                ));
    }
}
