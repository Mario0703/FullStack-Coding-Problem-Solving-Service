package minicode.com.controller.CoderExercutes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class DockerCodeStrategy implements CodeExercuteStrat {
    protected abstract Path sourceFile();

    protected abstract Path dockerFolder();

    protected abstract String imageName();

    protected abstract String containerNamePrefix();

    @Override
    public final String execute(String code) throws IOException, InterruptedException {
        Files.createDirectories(sourceFile().getParent());
        Files.writeString(sourceFile(), code, StandardCharsets.UTF_8);

        ProcessBuilder buildBuilder = new ProcessBuilder(
                "docker", "build", "-t", imageName(), "."
        );
        buildBuilder.directory(dockerFolder().toFile());
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
                "--memory=256m",
                "--cpus=0.5",
                "--pids-limit=64",
                "--user=65532:65532",
                imageName()
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
    }

    private String readProcessOutput(Process process) throws IOException {
        try (var inputStream = process.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
