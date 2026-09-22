package minicode.com.controller.CoderExercutes;

import java.nio.file.Path;

public class PythonStrat extends DockerCodeStrategy {
    private static final Path PYTHON_FOLDER = Path.of(
            "F:\\FullStackProjects\\MiniLeetCode\\CodeEnv\\python"
    );
    private static final Path PYTHON_FILE = PYTHON_FOLDER.resolve("main.py");

    @Override
    protected Path sourceFile() {
        return PYTHON_FILE;
    }

    @Override
    protected Path dockerFolder() {
        return PYTHON_FOLDER;
    }

    @Override
    protected String imageName() {
        return "minileetcode-python";
    }

    @Override
    protected String containerNamePrefix() {
        return "minicode-python";
    }
}
