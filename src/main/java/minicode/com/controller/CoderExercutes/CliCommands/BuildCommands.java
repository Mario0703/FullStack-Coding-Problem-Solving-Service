package minicode.com.controller.CoderExercutes.CliCommands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BuildCommands implements CLCommandsStrat {

    private final Path path;

    public BuildCommands(Path path) {
        this.path = path;
    }

    @Override
    public CommandResult buildContainer(String imageName) throws IOException, InterruptedException {
        List<String> commands = new ArrayList<>(List.of(
                "docker", "build","--progress=plain", "-t", imageName, "."
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
                "docker", "run", "--rm", "--name", containerName, imageName
        ));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(path.toFile());
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            return new CommandResult(124, "Execution timed out");
        }

        String output = readOutput(process);
        return new CommandResult(process.exitValue(), output);
    }

    private String readOutput(Process process) throws IOException {
        return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
