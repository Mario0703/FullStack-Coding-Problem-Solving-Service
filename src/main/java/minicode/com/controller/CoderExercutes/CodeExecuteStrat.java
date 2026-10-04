package minicode.com.controller.CoderExercutes;

import minicode.com.dto.Challenge.ExecutionResult;

import java.io.IOException;
import java.nio.file.Path;

public interface CodeExecuteStrat {
    ExecutionResult execute(String language, Path path) throws IOException, InterruptedException;
}


