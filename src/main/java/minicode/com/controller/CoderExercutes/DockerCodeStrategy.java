package minicode.com.controller.CoderExercutes;

import minicode.com.controller.CoderExercutes.CliCommands.CLCommandsStrat;
import minicode.com.controller.CoderExercutes.CliCommands.BuildCommands;
import minicode.com.controller.CoderExercutes.CliCommands.CLCommandsStrat.CommandResult;
import minicode.com.dto.Challenge.ExecutionResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DockerCodeStrategy implements CodeExecuteStrat {

    private static final Pattern TEST_RESULT =
            Pattern.compile("Passed: (\\d+), Failed: (\\d+)");

    @Override
    public ExecutionResult execute(Path path) throws IOException, InterruptedException {
        String imageName = "minileetcode-execution-" + UUID.randomUUID();

        CLCommandsStrat commands = new BuildCommands(path);

        CommandResult buildResult = commands.buildContainer(imageName);
        boolean isBuildSuccess = buildResult.exitCode() == 0;

        if (!isBuildSuccess) {
            throw new IOException("Failed to build Docker image: " + buildResult.output());
        }

        CommandResult runResult = commands.runContainer(imageName, imageName, 30);

        String output = runResult.output();

        findTestResults(output);

        int[] testResults = findTestResults(output);
        int passed = testResults[0];
        int failed = testResults[1];

        return new ExecutionResult(runResult.exitCode(), output, passed, failed);
    }

    private int[] findTestResults(String output) {
        Matcher matcher = TEST_RESULT.matcher(output);
        if (matcher.find()) {
            int passed = Integer.parseInt(matcher.group(1));
            int failed = Integer.parseInt(matcher.group(2));
            return new int[]{passed, failed};
        }
        return new int[]{0, 0};
    }
}
