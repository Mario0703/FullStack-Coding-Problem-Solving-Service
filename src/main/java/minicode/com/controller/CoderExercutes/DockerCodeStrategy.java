package minicode.com.controller.CoderExercutes;

import minicode.com.controller.CoderExercutes.CliCommands.CLIBuildCommands;
import minicode.com.controller.CoderExercutes.CliCommands.BuildCommands;
import minicode.com.controller.CoderExercutes.CliCommands.CLIBuildCommands.CommandResult;
import minicode.com.controller.CoderExercutes.Responses.CodeError;
import minicode.com.controller.CoderExercutes.Responses.CodeErrorCodesFactory;
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
        CLIBuildCommands commands = new BuildCommands(path);
        CommandResult buildResult = commands.buildContainer(imageName);
        Boolean isBuildSuccess = buildResult.exitCode() == 0;

        if (!isBuildSuccess) {

            CodeError error = CodeErrorCodesFactory.resolve(buildResult.exitCode(), buildResult.output());
            ExecutionResult executionResult = new ExecutionResult(
                    buildResult.exitCode(),
                    buildResult.output(),
                    0,
                    0,
                    error.errorCode()
            );

            return  executionResult;
        }
        int timeoutSeconds = 30;
        CommandResult runResult = commands.runContainer(imageName, imageName, timeoutSeconds);
        String output = runResult.output();

        int[] testResults = findTestResults(output);
        int passed = testResults[0];
        int failed = testResults[1];

        CodeError error = CodeErrorCodesFactory.resolve(runResult.exitCode(), output);
        ExecutionResult executionResult = new ExecutionResult(
                runResult.exitCode(),
                output,
                passed,
                failed,
                error.errorCode()
        );

        return executionResult;
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
