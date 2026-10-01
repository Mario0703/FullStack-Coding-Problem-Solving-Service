package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class PythonStrat extends DockerCodeStrategy {
    private static final Path PYTHON_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\python"
    );
    @Override
    protected Path dockerFolder() {
        return PYTHON_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-python";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-python";
    }

    @Override
    protected String submissionFileName() {
        return "submission.py";
    }

    @Override
    protected String combineSubmissionWithRunner(String submission, String runner) {
        return submission + System.lineSeparator() + System.lineSeparator() + runner;
    }
}



