package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Keeps the runner's own reporting untouched and cuts the compile and runtime
 * failure blocks down to the lines the strategy captures. When nothing is
 * captured the raw block is kept, so the user never loses the actual error.
 */
final class OutputFeedbackExtractor {

    // The runner's own reporting, as printed by CodeEnv/TestSuite/runCommandTest.bs.
    private static final String[] RUNNER_REPORTING_PREFIXES = {
            "Test case ",
            "Passed: ",
            "Actual output:",
            "Expected output:"
    };

    private final OutputFeedbackStrat strategy;
    private final StringBuilder feedback = new StringBuilder();
    private BlockState state = BlockState.NONE;
    private ErrorBlock block;

    private OutputFeedbackExtractor(OutputFeedbackStrat strategy) {
        this.strategy = strategy;
    }

    static String extract(OutputFeedbackStrat strategy, String output) {
        return new OutputFeedbackExtractor(strategy).extract(output);
    }

    private String extract(String output) {
        if (output == null || output.isBlank()) {
            return "";
        }

        for (String line : output.split("\\R", -1)) {
            processLine(line);
        }
        closeCurrentBlock();

        return feedback.toString().stripTrailing();
    }

    private void processLine(String line) {
        if (isInsideBlock()) {
            collectBlockLine(line);
            return;
        }
        processRegularLine(line);
    }

    private void processRegularLine(String line) {
        appendNormalized(line);
        if (line.startsWith(OutputFeedbackStrat.COMPILE_FAILURE_MARKER)) {
            openBlock(BlockState.COMPILE);
        } else if (line.startsWith(OutputFeedbackStrat.RUNTIME_FAILURE_MARKER)) {
            openBlock(BlockState.RUNTIME);
        }
    }

    private void collectBlockLine(String line) {
        boolean endsRuntimeBlock = state == BlockState.RUNTIME && isRunnerReportingLine(line);
        if (endsRuntimeBlock) {
            closeCurrentBlock();
            appendNormalized(line);
            return;
        }

        String normalizedLine = strategy.normalize(line);
        block.addRaw(normalizedLine);
        if (currentErrorPattern().matcher(line).find()) {
            block.capture(normalizedLine);
        }
    }

    private Pattern currentErrorPattern() {
        if (state == BlockState.COMPILE) {
            return strategy.compileErrorLinePattern();
        }
        return strategy.runtimeErrorLinePattern();
    }

    private void openBlock(BlockState newState) {
        state = newState;
        block = new ErrorBlock(strategy.newCapturedErrorCollector());
    }

    private void closeCurrentBlock() {
        if (!isInsideBlock()) {
            return;
        }
        feedback.append(block.render());
        state = BlockState.NONE;
        block = null;
    }

    private void appendNormalized(String line) {
        feedback.append(strategy.normalize(line)).append('\n');
    }

    private boolean isInsideBlock() {
        return state != BlockState.NONE;
    }

    private static boolean isRunnerReportingLine(String line) {
        String trimmed = line.stripLeading();
        for (String prefix : RUNNER_REPORTING_PREFIXES) {
            if (trimmed.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private enum BlockState {
        NONE,
        COMPILE,
        RUNTIME
    }

    /**
     * Renders the captured errors, or the raw block when nothing was captured.
     */
    private static final class ErrorBlock {

        private final StringBuilder rawLines = new StringBuilder();
        private final Collection<String> capturedErrors;

        private ErrorBlock(Collection<String> capturedErrors) {
            this.capturedErrors = capturedErrors;
        }

        private void addRaw(String line) {
            rawLines.append(line).append('\n');
        }

        private void capture(String line) {
            capturedErrors.add(line);
        }

        private String render() {
            if (capturedErrors.isEmpty()) {
                return rawLines.toString();
            }
            StringBuilder rendered = new StringBuilder();
            capturedErrors.forEach(line -> rendered.append(line).append('\n'));
            return rendered.toString();
        }
    }
}
