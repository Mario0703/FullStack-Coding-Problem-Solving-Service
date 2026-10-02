package minicode.com.controller.CoderExercutes;

import minicode.com.dto.User.ExecutionResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DockerCodeStrategy implements CodeExecuteStrat {

    private static final Pattern TEST_RESULT =
            Pattern.compile("Passed: (\\d+), Failed: (\\d+)");

    @Override
    public ExecutionResult execute(Path path) throws IOException, InterruptedException {
        String imageName = "minileetcode-execution-" + UUID.randomUUID();

        Process build = new ProcessBuilder(
                "docker", "build", "-t", imageName, "."
        ).directory(path.toFile()).redirectErrorStream(true).start();

        String buildOutput = readOutput(build);
        int buildExitCode = build.waitFor();
        if (buildExitCode != 0) {
            return new ExecutionResult(buildExitCode, buildOutput, 0, 1);
        }

        Process run = new ProcessBuilder(
                "docker", "run", "--rm", imageName
        ).directory(path.toFile()).redirectErrorStream(true).start();

        if (!run.waitFor(30, TimeUnit.SECONDS)) {
            run.destroyForcibly();
            return new ExecutionResult(124, "Execution timed out", 0, 1);
        }

        String output = readOutput(run);
        Matcher matcher = TEST_RESULT.matcher(output);
        int passed = 0;
        int failed = 1;
        if (matcher.find()) {
            passed = Integer.parseInt(matcher.group(1));
            failed = Integer.parseInt(matcher.group(2));
        }

        return new ExecutionResult(run.exitValue(), output, passed, failed);
    }

    private String readOutput(Process process) throws IOException {
        return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
