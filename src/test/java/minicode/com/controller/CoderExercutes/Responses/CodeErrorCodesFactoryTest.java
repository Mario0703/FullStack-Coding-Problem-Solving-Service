package minicode.com.controller.CoderExercutes.Responses;

import minicode.com.entity.Submissions.SubmissionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeErrorCodesFactoryTest {

    private SubmissionStatus resolve(int exitCode, String output) {
        return CodeErrorCodesFactory.resolve(exitCode, output).errorCode();
    }

    @Test
    public void testResolveReturnsAcceptedForZeroExitCode() {
        assertEquals(
                SubmissionStatus.ACCEPTED,
                resolve(0, "Test case 1 passed\n Passed: 1, Failed: 0")
        );
    }

    @Test
    public void testResolveDetectsCompilationFailure() {
        String output = "Compilation failed (exit code 1):\nMain.java:5: error: ';' expected";

        assertEquals(SubmissionStatus.COMPILE_ERROR, resolve(1, output));
    }

    @Test
    public void testResolveDetectsRuntimeFailure() {
        String output = "Runtime error on test case 1 (exit code 1):\nTraceback (most recent call last):";

        assertEquals(SubmissionStatus.RUNTIME_ERROR, resolve(1, output));
    }

    @Test
    public void testResolveDetectsTimeoutByExitCode() {
        assertEquals(
                SubmissionStatus.TIME_LIMIT,
                resolve(TimeLimitError.TIMEOUT_EXIT_CODE, "Passed: 0, Failed: 0")
        );
    }

    @Test
    public void testResolveDetectsTimeoutByOutput() {
        assertEquals(SubmissionStatus.TIME_LIMIT, resolve(1, "Execution timed out"));
    }

    @Test
    public void testResolveReturnsWrongAnswerForFailingTests() {
        String output = "Test case 1 failed\nActual output:\n7\nExpected output:\n9\n Passed: 0, Failed: 1";

        assertEquals(SubmissionStatus.WRONG_ANSWER, resolve(1, output));
    }

    @Test
    public void testResolveFallsBackToWrongAnswerForUnknownFailures() {
        assertEquals(SubmissionStatus.WRONG_ANSWER, resolve(1, "Something unexpected happened"));
    }

    @Test
    public void testResolvePrefersCompilationFailureOverRuntimeFailure() {
        String output =
                "Compilation failed (exit code 1):\nerror: expected declaration\n"
                        + "Runtime error on test case 1 (exit code 1):";

        assertEquals(SubmissionStatus.COMPILE_ERROR, resolve(1, output));
    }
}