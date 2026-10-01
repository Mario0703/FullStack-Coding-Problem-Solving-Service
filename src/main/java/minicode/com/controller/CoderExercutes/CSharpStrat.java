package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class CSharpStrat extends DockerCodeStrategy {
    private static final Path CSHARP_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\c#"
    );
    @Override
    protected Path dockerFolder() {
        return CSHARP_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-csharp";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-csharp";
    }

    @Override
    protected String submissionFileName() {
        return "submission.cs";
    }

    @Override
    protected String combineSubmissionWithRunner(String submission, String runner) {
        return runner + System.lineSeparator() + System.lineSeparator() + submission;
    }
}



