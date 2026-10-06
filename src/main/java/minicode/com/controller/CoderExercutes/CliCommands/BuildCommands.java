package minicode.com.controller.CoderExercutes.CliCommands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BuildCommands implements CLIBuildCommands {

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
                "--resource", "memory=1g,cpu-quota=100000",  // buildx syntax for cpu/memory
                "-t", imageName, "."
        ));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(path.toFile());
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        String output = readOutput(process);
        return new CommandResult(exitCode, output);
    }

    @Override
    public CommandResult runContainer(String containerName, String imageName, long timeoutSeconds)
            throws IOException, InterruptedException {

        List<String> commands = new ArrayList<>(List.of(
                "docker", "run", "--rm", "--name", containerName,
                "--network=none",
                "--memory=512m", "--memory-swap=512m",
                "--cpus=2",
                "--pids-limit=128",
                "--user=1000:1000",
                "--cap-drop=ALL",
                "--security-opt=no-new-privileges",
                "--tmpfs", "/tmp:rw,size=64m",
                imageName
        ));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(path.toFile());
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            int exitcode = 124; // Custom exit code for timeout
            return new CommandResult(exitcode, "Execution timed out");
        }

        String output = readOutput(process);
        return new CommandResult(process.exitValue(), output);
    }

    private String readOutput(Process process) throws IOException {
        return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
