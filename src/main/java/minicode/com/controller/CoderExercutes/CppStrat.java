package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class CppStrat extends DockerCodeStrategy {
    private static final Path CPP_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\c++"
    );
    private static final Path CPP_FILE = CPP_FOLDER.resolve("main.cpp");

    @Override
    protected Path sourceFile() {
        return CPP_FILE;
    }

    @Override
    protected Path dockerFolder() {
        return CPP_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-cpp";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-cpp";
    }
}
