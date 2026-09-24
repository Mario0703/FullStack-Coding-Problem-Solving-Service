package minicode.com.controller.CoderExercutes;

import java.io.IOException;
import java.nio.file.Path;

public interface CodeExercuteStrat {
    String execute(Path submissionFile, Path challengeFolder) throws IOException, InterruptedException;
}
