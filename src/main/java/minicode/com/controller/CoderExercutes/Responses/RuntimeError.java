package minicode.com.controller.CoderExercutes.Responses;

import minicode.com.entity.Submissions.SubmissionStatus;

public class RuntimeError implements CodeError {

    // Marker printed by CodeEnv/TestSuite/runCommandTest.bs when a test case
    // crashes at runtime. Keep it in sync with the script when changing its
    // output format.
    static final String RUNTIME_FAILURE_MARKER = "Runtime error on test case";

    @Override
    public boolean matches(int exitCode, String output) {
        return output.contains(RUNTIME_FAILURE_MARKER);
    }

    @Override
    public SubmissionStatus errorCode() {
        return SubmissionStatus.RUNTIME_ERROR;
    }
}