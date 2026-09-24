package minicode.com.controller.CoderExercutes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class DockerCodeStrategy implements CodeExercuteStrat {
    protected abstract Path dockerFolder();

    protected abstract String imageName();

    protected abstract String containerNamePrefix();

    protected abstract String submissionFileName();

    protected abstract String combineSubmissionWithRunner(String submission, String runner);

    @Override
    public final String execute(Path submissionFile, Path challengeFolder)
            throws IOException, InterruptedException {
        Path executionFolder = Files.createTempDirectory(dockerFolder(), "execution-");
        String executionImage = imageName() + "-" + UUID.randomUUID();

        try {
            prepareExecutionContext(executionFolder, submissionFile, challengeFolder);

            ProcessBuilder buildBuilder = new ProcessBuilder(
                    "docker", "build", "-t", executionImage, "."
            );
            buildBuilder.directory(executionFolder.toFile());
            buildBuilder.redirectErrorStream(true);

            Process buildProcess = buildBuilder.start();
            String buildOutput = readProcessOutput(buildProcess);
            int buildExitCode = buildProcess.waitFor();

            if (buildExitCode != 0) {
                throw new IOException(
                        "Docker image build failed with exit code "
                                + buildExitCode + "\n" + buildOutput
                );
            }

            String containerName = containerNamePrefix() + "-" + UUID.randomUUID();
            ProcessBuilder runBuilder = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "--name", containerName,
                    "--network=none",
                    "--read-only",
                    "--cap-drop=ALL",
                    "--security-opt=no-new-privileges",
                    "--tmpfs", "/tmp:rw,noexec,nosuid,size=64m",
                    "--memory=256m",
                    "--cpus=0.5",
                    "--pids-limit=64",
                    "--user=65532:65532",
                    executionImage
            );
            runBuilder.redirectErrorStream(true);

            Process runProcess = runBuilder.start();
            boolean finished = runProcess.waitFor(10, TimeUnit.SECONDS);

            if (!finished) {
                runProcess.destroyForcibly();

                Process cleanupProcess = new ProcessBuilder(
                        "docker", "rm", "-f", containerName
                ).start();
                cleanupProcess.waitFor(5, TimeUnit.SECONDS);

                throw new IOException("Code process exceeded the 10 second limit");
            }

            int runExitCode = runProcess.exitValue();
            String output = readProcessOutput(runProcess);
            return "Exit code: " + runExitCode + "\n" + output;
        } finally {
            deleteRecursively(executionFolder);
        }
    }

    private void prepareExecutionContext(
            Path executionFolder,
            Path submissionFile,
            Path challengeFolder
    ) throws IOException {
        Files.copy(
                dockerFolder().resolve("Dockerfile"),
                executionFolder.resolve("Dockerfile"),
                StandardCopyOption.REPLACE_EXISTING
        );
        copyDirectory(
                dockerFolder().getParent().resolve("TestSuite"),
                executionFolder.resolve("TestSuite")
        );
        copyDirectory(
                challengeFolder.resolve("TestCases"),
                executionFolder.resolve("TestCases")
        );
        Path runnerFile = findRunnerFile(challengeFolder.resolve("Runner"));
        String submission = Files.readString(submissionFile, StandardCharsets.UTF_8);
        String runner = Files.readString(runnerFile, StandardCharsets.UTF_8);
        Files.writeString(
                executionFolder.resolve(submissionFileName()),
                combineSubmissionWithRunner(submission, runner),
                StandardCharsets.UTF_8
        );
    }

    private Path findRunnerFile(Path runnerFolder) throws IOException {
        try (var files = Files.list(runnerFolder)) {
            return files
                    .filter(path -> path.getFileName().toString().startsWith("runner."))
                    .findFirst()
                    .orElseThrow(() -> new IOException("Challenge runner was not found"));
        }
    }

    private void copyDirectory(Path source, Path target) throws IOException {
        try (var paths = Files.walk(source)) {
            paths.forEach(path -> {
                try {
                    Path destination = target.resolve(source.relativize(path));
                    if (Files.isDirectory(path)) {
                        Files.createDirectories(destination);
                    } else {
                        Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            });
        } catch (RuntimeException exception) {
            if (exception.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw exception;
        }
    }

    private void deleteRecursively(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }

        try (var paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            });
        } catch (RuntimeException exception) {
            if (exception.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw exception;
        }
    }

    private String readProcessOutput(Process process) throws IOException {
        try (var inputStream = process.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
