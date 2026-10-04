package minicode.com.controller.CoderExercutes.Responses;

import minicode.com.entity.Submissions.SubmissionStatus;

public class TimeLimitError implements CodeError {

    // Exit code returned by BuildCommands when the container exceeds its
    // time budget, together with the output it reports in that case.
    static final int TIMEOUT_EXIT_CODE = 124;
    static final String TIMEOUT_OUTPUT = "Execution timed out";

    @Override
    public boolean matches(int exitCode, String output) {
        return exitCode == TIMEOUT_EXIT_CODE || output.contains(TIMEOUT_OUTPUT);
    }

    @Override
    public SubmissionStatus errorCode() {
        return SubmissionStatus.TIME_LIMIT;
    }
}