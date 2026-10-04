package minicode.com.controller.CoderExercutes.Responses;

import minicode.com.entity.Submissions.SubmissionStatus;

public interface CodeError {

    /**
     * Checks whether this error code applies to the finished execution.
     *
     * @param exitCode exit code reported by the container
     * @param output   combined stdout of the container run
     */
    boolean matches(int exitCode, String output);

    /**
     * The submission status this error code maps to.
     */
    SubmissionStatus errorCode();
}