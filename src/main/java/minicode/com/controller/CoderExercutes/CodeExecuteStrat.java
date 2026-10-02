package minicode.com.controller.CoderExercutes;

import minicode.com.dto.User.ExecutionResult;

import java.io.IOException;
import java.nio.file.Path;

public interface CodeExecuteStrat {
    ExecutionResult execute(Path path) throws IOException, InterruptedException;
}


