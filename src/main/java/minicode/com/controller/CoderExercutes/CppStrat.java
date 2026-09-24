package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class CppStrat extends DockerCodeStrategy {
    private static final Path CPP_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\c++"
    );
    @Override
    protected Path dockerFolder() {
        return CPP_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-cpp";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-cpp";
    }

    @Override
    protected String submissionFileName() {
        return "submission.cpp";
    }

    @Override
    protected String combineSubmissionWithRunner(String submission, String runner) {
        return submission + System.lineSeparator() + System.lineSeparator() + runner;
    }
}
