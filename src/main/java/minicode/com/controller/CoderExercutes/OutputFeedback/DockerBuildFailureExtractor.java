package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

/**
 * Cuts a docker build log (run with --progress=plain) down to its error lines.
 * The matched lines are kept verbatim because their paths belong to the build
 * context; when nothing matches the raw log is kept so the error is not lost.
 */
final class DockerBuildFailureExtractor {

    private static final Pattern DOCKER_ERROR_LINE = Pattern.compile("(?i)^.*(error|failed).*$|^\\s*>.*$");

    private DockerBuildFailureExtractor() {
    }

    static String extract(String buildOutput) {
        if (buildOutput == null || buildOutput.isBlank()) {
            return "";
        }

        String errorLines = collectErrorLines(buildOutput);
        if (errorLines.isEmpty()) {
            return buildOutput;
        }
        return errorLines;
    }

    private static String collectErrorLines(String buildOutput) {
        StringBuilder feedback = new StringBuilder();
        for (String line : buildOutput.split("\\R", -1)) {
            if (DOCKER_ERROR_LINE.matcher(line).find()) {
                feedback.append(line).append('\n');
            }
        }
        return feedback.toString().stripTrailing();
    }
}
