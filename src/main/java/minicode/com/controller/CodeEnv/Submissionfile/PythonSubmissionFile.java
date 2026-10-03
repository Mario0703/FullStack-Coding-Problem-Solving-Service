package minicode.com.controller.CodeEnv.Submissionfile;

import java.util.Objects;

public class PythonSubmissionFile implements SubmissionFileName {

    @Override
    public String fileName() {
        return "submission.py";
    }

    @Override
    public String combineCode(String runnerCode, String submissionCode) {
        return submissionCode + "\n" + Objects.requireNonNullElse(runnerCode, "");
    }
}
