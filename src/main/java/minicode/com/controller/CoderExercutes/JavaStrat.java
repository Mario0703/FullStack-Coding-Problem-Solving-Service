package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class JavaStrat extends DockerCodeStrategy {
    private static final Path JAVA_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\java"
    );
    @Override
    protected Path dockerFolder() {
        return JAVA_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-java";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-java";
    }

    @Override
    protected String submissionFileName() {
        return "submission.java";
    }

    @Override
    protected String combineSubmissionWithRunner(String submission, String runner) {
        return runner + System.lineSeparator() + System.lineSeparator() + submission;
    }
}
