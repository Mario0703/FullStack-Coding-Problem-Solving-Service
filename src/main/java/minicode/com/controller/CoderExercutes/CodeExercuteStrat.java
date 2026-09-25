package minicode.com.controller.CoderExercutes;

import minicode.com.dto.ExecutionResult;

import java.io.IOException;
import java.nio.file.Path;

public interface CodeExercuteStrat {
    ExecutionResult execute(Path submissionFile, Path challengeFolder) throws IOException, InterruptedException;
}
