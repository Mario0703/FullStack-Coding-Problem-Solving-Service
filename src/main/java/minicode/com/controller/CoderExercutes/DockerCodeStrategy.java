package minicode.com.controller.CoderExercutes;

import minicode.com.controller.CoderExercutes.CliCommands.CLIBuildCommands;
import minicode.com.controller.CoderExercutes.CliCommands.BuildCommands;
import minicode.com.controller.CoderExercutes.CliCommands.CLIBuildCommands.CommandResult;
import minicode.com.controller.CoderExercutes.Responses.CodeError;
import minicode.com.controller.CoderExercutes.Responses.CodeErrorCodesFactory;
import minicode.com.controller.CoderExercutes.OutputFeedback.OutputFeedbackFactory;
import minicode.com.controller.CoderExercutes.OutputFeedback.OutputFeedbackStrat;
import minicode.com.dto.Challenge.ExecutionResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DockerCodeStrategy implements CodeExecuteStrat {

    private static final Pattern TEST_RESULT = Pattern.compile("Passed: (\\d+), Failed: (\\d+)");
    // Regex group indexes inside TEST_RESULT; group 0 is the whole match.
    private static final int passingTestGroup = 1;
    private static final int failingTestGroup = 2;
    private static final int timeoutSeconds = 30;
    private static final int passingTestCasesForBuildFailure = 0;
    private static final int failingTestCasesForBuildFailure = 0;


    @Override
    public ExecutionResult execute(String language, Path path) throws IOException, InterruptedException {
        String imageName = "minileetcode-execution-" + UUID.randomUUID();

        // The per-language strategy knows how to cut the raw docker output
        // down to the feedback that is relevant to the user.
        OutputFeedbackStrat feedback = OutputFeedbackFactory.forLanguage(language);
        CLIBuildCommands commands = new BuildCommands(path);

        CommandResult buildResult = commands.buildContainer(imageName);
        Boolean isBuildSuccess = buildResult.exitCode() == 0;

        //build failed so just return the build error and exit code, no need to run the container
        if (!isBuildSuccess) {
            CodeError error = CodeErrorCodesFactory.resolve(buildResult.exitCode(), buildResult.output());
            ExecutionResult executionResult = new ExecutionResult(
                    buildResult.exitCode(),
                    feedback.extractBuildFailure(buildResult.output()),
                    passingTestCasesForBuildFailure,
                    failingTestCasesForBuildFailure,
                    error.errorCode()
            );

            return  executionResult;
        }
        CommandResult runResult = commands.runContainer(imageName, imageName, timeoutSeconds);
        String output = runResult.output();

        int[] testResults = findTestResults(output);

        int passed = testResults[0];
        int failed = testResults[1];

        CodeError error = CodeErrorCodesFactory.resolve(runResult.exitCode(), output);
        ExecutionResult executionResult = new ExecutionResult(
                runResult.exitCode(),
                feedback.extractFeedback(output),
                passed,
                failed,
                error.errorCode()
        );

        return executionResult;
    }

    private int[] findTestResults(String output) {
        Matcher matcher = TEST_RESULT.matcher(output);
        if (matcher.find()) {
            int passed = Integer.parseInt(matcher.group(passingTestGroup));
            int failed = Integer.parseInt(matcher.group(failingTestGroup));
            return new int[]{passed, failed};
        }
        return new int[]{0, 0}; // just return empty if we did not find any test results
    }
}
