package minicode.com.controller.CodeEnv.Submissionfile;

import java.util.Objects;

public class CsharpSubmissionFile implements SubmissionFileName {

    @Override
    public String fileName() {
        return "submission.cs";
    }

    @Override
    public String combineCode(String runnerCode, String submissionCode) {
        return Objects.requireNonNullElse(runnerCode, "") + "\n" + submissionCode;
    }
}
