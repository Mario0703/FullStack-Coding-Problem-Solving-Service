package minicode.com.controller.CoderExercutes.CliCommands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BuildCommands implements CLIBuildCommands {

    // A build must not run forever: a stuck build would hold the request
    // thread and the builder. Compilation happens at run time, so the build
    // itself only copies files and prepares the project.
    private static final long BUILD_TIMEOUT_SECONDS = 180;
    // Time the cleanup commands (docker rm/image rm) are allowed to take.
    private static final long CLEANUP_TIMEOUT_SECONDS = 30;
    private static final int TIMEOUT_EXIT_CODE = 124;

    private final Path path;

    public BuildCommands(Path path) {
        this.path = path;
    }

    @Override
    public CommandResult buildContainer(String imageName) throws IOException, InterruptedException {
        List<String> commands = new ArrayList<>(List.of(
                "docker", "build",
                "--progress=plain",
                "--network=none",
                // buildx takes one key=value per --resource flag, so repeat it.
                // cpu-quota is microseconds of CPU per 100ms period => 1 CPU.
                "--resource", "memory=1g",
                "--resource", "cpu-quota=100000",
                "-t", imageName, "."
        ));

        Process process = start(commands);
        boolean finished = process.waitFor(BUILD_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            // Drop any partially created image so failed builds do not pile up.
            removeImage(imageName);
            return new CommandResult(
                    TIMEOUT_EXIT_CODE,
                    "Build timed out after " + BUILD_TIMEOUT_SECONDS + " seconds"
            );
        }

        String output = readOutput(process);
        return new CommandResult(process.exitValue(), output);
    }

    @Override
    public CommandResult runContainer(String containerName, String imageName, long timeoutSeconds)
            throws IOException, InterruptedException {

        List<String> commands = new ArrayList<>(List.of(
                "docker", "run", "--rm", "--name", containerName,
                "--network=none",
                "--memory=512m", "--memory-swap=512m",
                "--cpu-period=50000",
                "--cpu-quota=25000",
                "--pids-limit=128",
                "--user=1000:1000",
                "--cap-drop=ALL",
                "--security-opt=no-new-privileges",
                "--tmpfs", "/tmp:rw,size=256m",
                imageName
        ));

        Process process = start(commands);
        boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

        if (!finished) {
            // Killing the `docker run` client only detaches it: the container
            // keeps running. Remove it forcibly so the runaway process really
            // stops and frees its memory/CPU, then report the timeout.
            process.destroyForcibly();
            forceRemoveContainer(containerName);
            return new CommandResult(TIMEOUT_EXIT_CODE, "Execution timed out");
        }

        String output = readOutput(process);
        return new CommandResult(process.exitValue(), output);
    }

    @Override
    public CommandResult removeImage(String imageName) throws IOException, InterruptedException {
        return runCleanup(List.of("docker", "image", "rm", "-f", imageName));
    }

    private void forceRemoveContainer(String containerName) throws IOException, InterruptedException {
        // -f kills the container if it is still running, then removes it.
        runCleanup(List.of("docker", "rm", "-f", containerName));
    }

    // Best-effort cleanup: a missing container/image is not an error.
    private CommandResult runCleanup(List<String> commands) throws IOException, InterruptedException {
        Process process = start(commands);
        if (!process.waitFor(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            return new CommandResult(TIMEOUT_EXIT_CODE, "Cleanup timed out");
        }
        return new CommandResult(process.exitValue(), readOutput(process));
    }

    private Process start(List<String> commands) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(path.toFile());
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private String readOutput(Process process) throws IOException {
        return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
