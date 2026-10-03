package minicode.com.controller.CoderExercutes.ExerCuterDefition;

import java.nio.file.Path;

public class CsharpExecutionDefinition implements ExecutionDefinition {

    @Override
    public String language() {
        return "csharp";
    }

    @Override
    public String imageName() {
        return "minileetcode-csharp";
    }

    @Override
    public Path dockerfilePath() {
        return Path.of("CodeEnv", language(), "Dockerfile");
    }

    @Override
    public Path testRunnerPath() {
        return Path.of("CodeEnv", "TestSuite", "runCommandTest.bs");
    }
}
