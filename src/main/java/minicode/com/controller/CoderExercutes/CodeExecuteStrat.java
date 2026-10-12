package minicode.com.controller.CoderExercutes;

import minicode.com.dto.Challenge.Response.ExecutionResultResponse;

import java.io.IOException;
import java.nio.file.Path;

public interface CodeExecuteStrat {
    ExecutionResultResponse execute(String language, Path path) throws IOException, InterruptedException;
}


