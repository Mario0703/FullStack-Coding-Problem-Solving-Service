package minicode.com.controller.CoderExercutes.ExerCuterDefition;

import java.nio.file.Path;

public interface ExecutionDefinition {
    String language();
    String imageName();
    Path dockerfilePath();
    Path testRunnerPath();
}
