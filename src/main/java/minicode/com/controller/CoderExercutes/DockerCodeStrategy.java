package minicode.com.controller.CoderExercutes;

import minicode.com.controller.CoderExercutes.CliCommands.CLIBuildCommands;
import minicode.com.controller.CoderExercutes.CliCommands.BuildCommands;
import minicode.com.controller.CoderExercutes.CliCommands.CLIBuildCommands.CommandResult;
import minicode.com.controller.CoderExercutes.Responses.CodeError;
import minicode.com.controller.CoderExercutes.Responses.CodeErrorCodesFactory;
import minicode.com.controller.CoderExercutes.OutputFeedback.OutputFeedbackFactory;
import minicode.com.controller.CoderExercutes.OutputFeedback.OutputFeedbackStrat;
import minicode.com.dto.Challenge.Response.ExecutionResultResponse;

import java.nio.file.Path;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DockerCodeStrategy implements CodeExecuteStrat {

    private static final Pattern TEST_RESULT = Pattern.compile("Passed: (\\d+), Failed: (\\d+)");
    private static final int passingTestGroup = 1;
    private static final int failingTestGroup = 2;
    private static final int timeoutSeconds = 30;

    @Override
    public ExecutionResultResponse execute(String language, Path path) throws InterruptedException {
        String imageName = "minileetcode-execution-" + UUID.randomUUID();

        OutputFeedbackStrat feedback = OutputFeedbackFactory.forLanguage(language);
        CLIBuildCommands commands = new BuildCommands(path);

        try {
            commands.buildContainer(imageName);

            CommandResult runResult = commands.runContainer(imageName, imageName, timeoutSeconds);
            String output = runResult.output();

            int[] testResults = findTestResults(output);

            CodeError error = CodeErrorCodesFactory.resolve(runResult.exitCode(), output);
            return new ExecutionResultResponse(
                    runResult.exitCode(),
                    feedback.extractFeedback(output),
                    testResults[0],
                    testResults[1],
                    error.errorCode()
            );
        } finally {
            removeImageQuietly(commands, imageName);
        }
    }

    private void removeImageQuietly(CLIBuildCommands commands, String imageName) {
        try {
            commands.removeImage(imageName);
        } catch (Exception e) {
            System.err.println("Failed to remove Docker image: " + e.getMessage());
        }
    }

    private int[] findTestResults(String output) {
        Matcher matcher = TEST_RESULT.matcher(output);
        if (matcher.find()) {
            int passed = Integer.parseInt(matcher.group(passingTestGroup));
            int failed = Integer.parseInt(matcher.group(failingTestGroup));
            return new int[]{passed, failed};
        }
        return new int[]{0, 0};
    }
}
