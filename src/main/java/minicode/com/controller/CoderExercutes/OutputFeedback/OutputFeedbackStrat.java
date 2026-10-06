package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;

public interface OutputFeedbackStrat {

    /**
     * Marker printed by CodeEnv/TestSuite/runCommandTest.bs when the compile
     * step fails. Everything after it is raw compiler output. Keep it in sync
     * with the script when changing its output format.
     */
    String COMPILE_FAILURE_MARKER = "Compilation failed";

    /**
     * Marker printed by CodeEnv/TestSuite/runCommandTest.bs when a test case
     * crashes at runtime. Everything after it is the program's crash output,
     * until the runner's own reporting continues. Keep it in sync with the
     * script when changing its output format.
     */
    String RUNTIME_FAILURE_MARKER = "Runtime error on test case";

    /**
     * The regex that captures the lines of this language's compiler output
     * that are relevant feedback (syntax/compile errors and their context).
     * Lines that do not match are cut from the compile block as junk.
     */
    Pattern compileErrorLinePattern();

    /**
     * The regex that captures the lines of this language's runtime crash
     * output that are relevant feedback. Defaults to matching everything so
     * the whole crash output is kept; Python overrides it because a traceback
     * is mostly File/source/caret noise next to its final exception line.
     */
    default Pattern runtimeErrorLinePattern() {
        return Pattern.compile(".*");
    }

    /**
     * Removes container-internal noise (e.g. /app/ paths) from a feedback line.
     */
    String normalize(String line);

    /**
     * Whether repeated identical diagnostic lines should be dropped. MSBuild
     * prints every diagnostic twice (once live, once in the failure summary),
     * so C# enables this.
     */
    default boolean deduplicatesFeedback() {
        return false;
    }

    /**
     * Extracts the feedback relevant to the user from the raw container
     * output. The test runner's own reporting (test case results, summary) is
     * kept as-is. The compile block is cut down to the lines captured by
     * {@link #compileErrorLinePattern()}, and the runtime error block to the
     * lines captured by {@link #runtimeErrorLinePattern()}. If a pattern
     * captures nothing because the output has an unexpected format, the raw
     * block is kept so the user never loses the actual error.
     */
    default String extractFeedback(String output) {
        boolean hasEmptyOutput = output == null || output.isBlank();
        if (hasEmptyOutput) {
            return "";
        }

        StringBuilder feedback = new StringBuilder();
        StringBuilder rawBlock = new StringBuilder();
        Collection<String> capturedErrors = new ArrayList<>();
        boolean inCompileBlock = false;
        boolean inRuntimeBlock = false;

        for (String line : output.split("\\R", -1)) {
            boolean hasOpenBlock = !inCompileBlock && !inRuntimeBlock;
            if (hasOpenBlock) {
                feedback.append(normalize(line)).append('\n');
                if (line.startsWith(COMPILE_FAILURE_MARKER)) {
                    inCompileBlock = true;
                    rawBlock.setLength(0);
                    boolean hasDeduplicatedFeedback = deduplicatesFeedback();
                    if (hasDeduplicatedFeedback) {
                        capturedErrors = new LinkedHashSet<>();
                    } else {
                        capturedErrors = new ArrayList<>();
                    }
                } else if (line.startsWith(RUNTIME_FAILURE_MARKER)) {
                    inRuntimeBlock = true;
                    rawBlock.setLength(0);
                    capturedErrors = new ArrayList<>();
                }
                continue;
            }

            // The runner's own reporting (further test cases, the summary)
            // ends the runtime error block.
            boolean hasRuntimeReporting = inRuntimeBlock && isRunnerReportingLine(line);
            if (hasRuntimeReporting) {
                appendCapturedOrRawBlock(feedback, rawBlock, capturedErrors);
                rawBlock.setLength(0);
                capturedErrors.clear();
                inRuntimeBlock = false;
                feedback.append(normalize(line)).append('\n');
                continue;
            }

            rawBlock.append(normalize(line)).append('\n');
            Pattern errorLinePattern = runtimeErrorLinePattern();
            if (inCompileBlock) {
                errorLinePattern = compileErrorLinePattern();
            }
            if (errorLinePattern.matcher(line).find()) {
                capturedErrors.add(normalize(line));
            }
        }

        boolean hasUnfinishedBlock = inCompileBlock || inRuntimeBlock;
        if (hasUnfinishedBlock) {
            appendCapturedOrRawBlock(feedback, rawBlock, capturedErrors);
        }

        return feedback.toString().stripTrailing();
    }

    /**
     * Appends the feedback for a finished block: the lines the language
     * pattern captured, or the raw block if nothing was captured.
     */
    private static void appendCapturedOrRawBlock(
            StringBuilder feedback,
            StringBuilder rawBlock,
            Collection<String> capturedErrors
    ) {
        if (capturedErrors.isEmpty()) {
            feedback.append(rawBlock);
        } else {
            capturedErrors.forEach(line -> feedback.append(line).append('\n'));
        }
    }

    /**
     * Whether a line is the test runner's own reporting rather than program
     * output (see CodeEnv/TestSuite/runCommandTest.bs).
     */
    private static boolean isRunnerReportingLine(String line) {
        String trimmed = line.stripLeading();
        boolean hasRunnerReporting = trimmed.startsWith("Test case ")
                || trimmed.startsWith("Passed: ")
                || trimmed.startsWith("Actual output:")
                || trimmed.startsWith("Expected output:");
        return hasRunnerReporting;
    }

    /**
     * Cuts the raw docker build log down to its error lines when the image
     * build itself fails. BuildCommands runs docker build with
     * --progress=plain, so the log reports every step; only the error and
     * failure lines are relevant feedback for the user. The matched lines
     * are kept verbatim because their paths belong to the build context.
     * If nothing matches, the raw log is kept so the user never loses the
     * actual error.
     */
    default String extractBuildFailure(String buildOutput) {
        boolean hasEmptyBuildOutput = buildOutput == null || buildOutput.isBlank();
        if (hasEmptyBuildOutput) {
            return "";
        }

        Pattern dockerErrorLine = Pattern.compile("(?i)^.*(error|failed).*$|^\\s*>.*$");
        StringBuilder feedback = new StringBuilder();

        for (String line : buildOutput.split("\\R", -1)) {
            if (dockerErrorLine.matcher(line).find()) {
                feedback.append(line).append('\n');
            }
        }

        if (feedback.isEmpty()) {
            return buildOutput;
        }

        return feedback.toString().stripTrailing();
    }
}
