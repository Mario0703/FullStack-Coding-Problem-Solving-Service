package minicode.com.controller.CoderExercutes.Responses;

import minicode.com.entity.Submissions.SubmissionStatus;

public class CompilationError implements CodeError {

    // Marker printed by CodeEnv/TestSuite/runCommandTest.bs when the compile
    // step fails. Keep it in sync with the script when changing its output.
    static final String COMPILE_FAILURE_MARKER = "Compilation failed";

    @Override
    public boolean matches(int exitCode, String output) {
        return output.contains(COMPILE_FAILURE_MARKER);
    }

    @Override
    public SubmissionStatus errorCode() {
        return SubmissionStatus.COMPILE_ERROR;
    }
}