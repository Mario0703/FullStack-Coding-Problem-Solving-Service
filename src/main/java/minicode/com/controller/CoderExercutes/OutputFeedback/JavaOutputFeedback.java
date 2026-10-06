package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

public class JavaOutputFeedback implements OutputFeedbackStrat {

    // javac output has no banners, so the diagnostic itself, the quoted
    // source line, the caret pointer, the symbol/location details and the
    // final count line are all captured:
    //   /app/Main.java:6: error: ';' expected
    //           int a = scanner.nextInt()
    //                                    ^
    //   /app/Main.java:3: error: cannot find symbol
    //     symbol:   variable Solution
    //     location: class Main
    //   2 errors
    private static final Pattern JAVAC_ERRORS = Pattern.compile(
            "^(?:"
                    + "\\S+\\.java:\\d+(?::\\d+)?:\\s*(?:error|warning|note):.*"
                    + "|\\s+\\S.*"
                    + "|\\d+ errors?(?:,\\s*\\d+ warnings?)?"
                    + ")$"
    );

    @Override
    public Pattern compileErrorLinePattern() {
        return JAVAC_ERRORS;
    }

    @Override
    public String normalize(String line) {
        return line.replace("/app/", "");
    }
}