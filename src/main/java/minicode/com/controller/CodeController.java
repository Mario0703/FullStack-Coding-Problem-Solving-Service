package minicode.com.controller;

import minicode.com.dto.CodeSubmission;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/code/playground")
public class CodeController {
    @PostMapping("/submit")
    public String submitCode(@RequestBody CodeSubmission submission) throws IOException {
        String language = submission.language();
        String code = submission.code();
        FileWriter fileWriter;

        switch (language) {
            case "python":
                try {
                    Path pythonFolder = Path.of(
                            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\python"
                    );

                    Files.writeString(
                            pythonFolder.resolve("main.py"),
                            code,
                            StandardCharsets.UTF_8
                    );

                    ProcessBuilder buildBuilder = new ProcessBuilder(
                            "docker", "build", "-t", "minileetcode-python", "."
                    );
                    buildBuilder.directory(pythonFolder.toFile());
                    buildBuilder.redirectErrorStream(true);

                    Process buildProcess = buildBuilder.start();
                    String buildOutput = readProcessOutput(buildProcess);
                    int buildExitCode = buildProcess.waitFor();

                    if (buildExitCode != 0) {
                        throw new IllegalStateException(
                                "Docker image build failed with exit code "
                                        + buildExitCode + "\n" + buildOutput
                        );
                    }

                    String containerName = "minicode-python-" + UUID.randomUUID();
                    ProcessBuilder runBuilder = new ProcessBuilder(
                            "docker", "run", "--rm",
                            "--name", containerName,
                            "--network=none",
                            "--read-only",
                            "--cap-drop=ALL",
                            "--security-opt=no-new-privileges",
                            "--memory=256m",
                            "--cpus=0.5",
                            "--pids-limit=64",
                            "--user=65532:65532",
                            "minileetcode-python"
                    );
                    runBuilder.redirectErrorStream(true);

                    Process runProcess = runBuilder.start();
                    boolean finished = runProcess.waitFor(10, TimeUnit.SECONDS);

                    if (!finished) {
                        runProcess.destroyForcibly();

                        Process cleanupProcess = new ProcessBuilder(
                                "docker", "rm", "-f", containerName
                        ).inheritIO().start();
                        cleanupProcess.waitFor(5, TimeUnit.SECONDS);

                        throw new IOException("Python process exceeded the 10 second limit");
                    }

                    int runExitCode = runProcess.exitValue();
                    String output = readProcessOutput(runProcess);
                    return "Exit code: " + runExitCode + "\n" + output;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Python Docker process was interrupted", e);
                } catch (IOException | RuntimeException e) {
                    throw new IOException("Could not run Python code in Docker", e);
                }
            case "java":
                try {
                    fileWriter = new FileWriter("Main.java");
                    fileWriter.write(code);
                    fileWriter.close();
                } catch (Exception exception) {
                    System.out.println("Error writing to file: " + exception.getMessage());
                }
                break;
            case "cpp":
                try {
                    fileWriter = new FileWriter("main.cpp");
                    fileWriter.write(code);
                    fileWriter.close();
                } catch (Exception exception) {
                    System.out.println("Error writing to file: " + exception.getMessage());
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }

        return "Code saved for " + language;
    }

    private String readProcessOutput(Process process) throws IOException {
        try (var inputStream = process.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
