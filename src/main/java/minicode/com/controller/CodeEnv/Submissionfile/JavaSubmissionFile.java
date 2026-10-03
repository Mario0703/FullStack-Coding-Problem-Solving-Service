package minicode.com.controller.CodeEnv.Submissionfile;

import java.util.Objects;

public class JavaSubmissionFile implements SubmissionFileName {

    @Override
    public String fileName() {
        return "submission.java";
    }

    @Override
    public String combineCode(String runnerCode, String submissionCode) {
        return Objects.requireNonNullElse(runnerCode, "") + "\n" + submissionCode;
    }
}
