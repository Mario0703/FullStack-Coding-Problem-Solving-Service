package minicode.com.controller.CoderExercutes.OutputFeedback;

import java.util.regex.Pattern;

public class CsharpOutputFeedback implements OutputFeedbackStrat {

    // dotnet build wraps diagnostics in a wall of MSBuild noise (engine
    // banner, restore log, per-project summary, "Build FAILED." section,
    // "Time Elapsed") and prints every diagnostic twice. Only the actual
    // compiler diagnostics are captured:
    //   Program.cs(5, 13): error CS1002: ; expected [/app/app.csproj]
    //   CSC : error CS5001: Program does not have an entry point ...
    private static final Pattern DOTNET_ERRORS = Pattern.compile(
            "^(?:"
                    + ".*\\.cs\\(\\d+(?:,\\s*\\d+)?\\):\\s*(?:error|warning)\\s+CS\\d+:.*"
                    + "|(?:CSC|MSBUILD)\\s*:\\s*(?:error|warning)\\s+CS\\d+:.*"
                    + ")$"
    );

    @Override
    public Pattern compileErrorLinePattern() {
        return DOTNET_ERRORS;
    }

    @Override
    public boolean deduplicatesFeedback() {
        // dotnet build repeats every diagnostic in the failure summary.
        return true;
    }

    @Override
    public String normalize(String line) {
        // Drop the trailing project annotation, e.g. " [/app/app.csproj]".
        return line.replaceAll("\\s*\\[[^\\]]*\\]$", "").replace("/app/", "");
    }
}