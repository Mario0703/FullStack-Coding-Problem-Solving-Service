package minicode.com.controller.CoderExercutes.Responses;

import minicode.com.entity.Submissions.SubmissionStatus;

public class WrongAnswerError implements CodeError {

    @Override
    public boolean matches(int exitCode, String output) {
        // The program ran to completion but produced wrong output. This
        // strategy is the fallback: it matches any non-accepted execution
        // that no more specific error code claimed.
        return true;
    }

    @Override
    public SubmissionStatus errorCode() {
        return SubmissionStatus.WRONG_ANSWER;
    }
}