package minicode.com.controller.CodeEnv.Submissionfile;

import java.util.Objects;

public class CplusplusSubmissionFile implements SubmissionFileName {

    @Override
    public String fileName() {
        return "submission.cpp";
    }

    @Override
    public String combineCode(String runnerCode, String submissionCode) {
        return submissionCode + "\n" + Objects.requireNonNullElse(runnerCode, "");
    }
}
