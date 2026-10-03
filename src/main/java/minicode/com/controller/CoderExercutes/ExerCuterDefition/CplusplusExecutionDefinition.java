package minicode.com.controller.CoderExercutes.ExerCuterDefition;

import java.nio.file.Path;

public class CplusplusExecutionDefinition implements ExecutionDefinition {

    @Override
    public String language() {
        return "cplusplus";
    }

    @Override
    public String imageName() {
        return "minileetcode-cpp";
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
