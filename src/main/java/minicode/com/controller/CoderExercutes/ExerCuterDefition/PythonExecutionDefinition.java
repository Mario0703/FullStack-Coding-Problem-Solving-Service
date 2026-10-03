package minicode.com.controller.CoderExercutes.ExerCuterDefition;

import java.nio.file.Path;

public class PythonExecutionDefinition implements ExecutionDefinition {

    @Override
    public String language() {
        return "python";
    }

    @Override
    public String imageName() {
        return "minileetcode-python";
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
