package minicode.com.controller.CodeEnv;

import java.io.IOException;
import java.nio.file.Path;

public interface EnvStrat {

    Path createEnv() throws IOException;

    void destroyEnv(Path path) throws IOException;
}
