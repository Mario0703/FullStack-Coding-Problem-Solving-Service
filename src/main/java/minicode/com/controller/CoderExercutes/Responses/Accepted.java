package minicode.com.controller.CoderExercutes.Responses;

import minicode.com.entity.Submissions.SubmissionStatus;

public class Accepted implements CodeError {

    @Override
    public boolean matches(int exitCode, String output) {
        return exitCode == 0;
    }

    @Override
    public SubmissionStatus errorCode() {
        return SubmissionStatus.ACCEPTED;
    }
}