package minicode.com.controller.CoderExercutes;

import java.io.IOException;

public interface CodeExercuteStrat {
    String execute(String code) throws IOException, InterruptedException;
}
