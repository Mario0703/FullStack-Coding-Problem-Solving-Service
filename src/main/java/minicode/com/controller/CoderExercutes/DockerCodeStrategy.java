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
    private int passingTestIndex = 0;
    private int failingTestIndex = 1;

    @Override
    public ExecutionResult execute(Path path) throws IOException, InterruptedException {
        String imageName = "minileetcode-execution-" + UUID.randomUUID();
        CLIBuildCommands commands = new BuildCommands(path);
        CommandResult buildResult = commands.buildContainer(imageName);
        Boolean isBuildSuccess = buildResult.exitCode() == 0;

        //build failed so just return the build error and exit code, no need to run the container
        if (!isBuildSuccess) {
            CodeError error = CodeErrorCodesFactory.resolve(buildResult.exitCode(), buildResult.output());
            int failingTests = 0;
            int passingTests = 0;
            ExecutionResult executionResult = new ExecutionResult(
                    buildResult.exitCode(),
                    buildResult.output(),
                    passingTests,
                    failingTests,
                    error.errorCode()
            );

            return  executionResult;
        }
        int timeoutSeconds = 30;
        CommandResult runResult = commands.runContainer(imageName, imageName, timeoutSeconds);
        String output = runResult.output();

        int[] testResults = findTestResults(output);

        int passed = testResults[passingTestIndex];
        int failed = testResults[failingTestIndex];

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
            int passed = Integer.parseInt(matcher.group(passingTestIndex));
            int failed = Integer.parseInt(matcher.group(failingTestIndex));
            return new int[]{passed, failed};
        }
        return new int[]{0, 0}; // just return empty if we did not find any test results
    }
}
