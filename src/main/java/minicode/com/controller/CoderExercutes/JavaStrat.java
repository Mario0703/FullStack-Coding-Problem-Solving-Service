package minicode.com.controller.CoderExercutes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaStrat implements CodeExercuteStrat {
    private static final Path JAVA_FILE = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\java\\Main.java"
    );

    @Override
    public String execute(String code) throws IOException {
        Files.writeString(
                JAVA_FILE,
                code,
                StandardCharsets.UTF_8
        );
        return "Code saved for java";
    }
}
