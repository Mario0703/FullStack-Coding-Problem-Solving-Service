package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public interface OutputFeedbackStrat {

    // Printed by CodeEnv/TestSuite/runCommandTest.bs; keep in sync with it.
    String COMPILE_FAILURE_MARKER = "Compilation failed";
    String RUNTIME_FAILURE_MARKER = "Runtime error on test case";

    String CONTAINER_ROOT = "/app/";

    /**
     * The regex capturing the compiler output lines that are relevant feedback.
     * Lines that do not match are cut from the compile block as junk.
     */
    Pattern compileErrorLinePattern();

    /**
     * The regex capturing the runtime crash lines that are relevant feedback.
     * Defaults to matching everything; Python overrides it because a traceback
     * is mostly noise next to its final exception line.
     */
    default Pattern runtimeErrorLinePattern() {
        return Pattern.compile(".*");
    }

    String normalize(String line);

    /**
     * The collection gathering captured error lines. Returning a
     * {@link java.util.LinkedHashSet} collapses repeated identical lines.
     */
    default Collection<String> newCapturedErrorCollector() {
        return new ArrayList<>();
    }

    default String extractFeedback(String output) {
        return OutputFeedbackExtractor.extract(this, output);
    }

    default String extractBuildFailure(String buildOutput) {
        return DockerBuildFailureExtractor.extract(buildOutput);
    }
}
