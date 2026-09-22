package minicode.com.controller.CoderExercutes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CppStrat implements CodeExercuteStrat {
    private static final Path CPP_FILE = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\c++\\main.cpp"
    );

    @Override
    public String execute(String code) throws IOException {
        Files.writeString(
                CPP_FILE,
                code,
                StandardCharsets.UTF_8
        );
        return "Code saved for cpp";
    }
}
