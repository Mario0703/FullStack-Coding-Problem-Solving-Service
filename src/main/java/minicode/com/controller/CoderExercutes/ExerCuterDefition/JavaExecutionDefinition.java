package minicode.com.controller.CoderExercutes.ExerCuterDefition;

import java.nio.file.Path;

public class JavaExecutionDefinition implements ExecutionDefinition {

    @Override
    public String language() {
        return "java";
    }

    @Override
    public String imageName() {
        return "minileetcode-java";
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
