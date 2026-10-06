package minicode.com.controller.CoderExercutes.Responses;

import java.util.List;

public final class CodeErrorCodesFactory {

    // The order defines precedence: the first strategy whose matches(...)
    // returns true wins. WrongAnswerErrorCodesStrat always matches and must
    // stay last so unknown failures still resolve to a status.
    private static final List<CodeError> ERROR_CODES = List.of(
            new Accepted(),
            new TimeLimitError(),
            new CompilationError(),
            new RuntimeError(),
            new WrongAnswerError()
    );

    private CodeErrorCodesFactory() {
    }

    /**
     * Resolves the error code strategy that applies to a finished execution.
     *
     * @param exitCode exit code reported by the container
     * @param output   combined stdout of the container run
     */
    public static CodeError resolve(int exitCode, String output) {
        return ERROR_CODES.stream()
                .filter(errorCode -> errorCode.matches(exitCode, output))
                .findFirst()
                .orElseGet(WrongAnswerError::new);
    }
}