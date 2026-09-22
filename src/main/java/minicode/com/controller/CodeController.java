package minicode.com.controller;

import minicode.com.controller.CoderExercutes.CodeExercuteStrat;
import minicode.com.controller.CoderExercutes.CSharpStrat;
import minicode.com.controller.CoderExercutes.CppStrat;
import minicode.com.controller.CoderExercutes.JavaStrat;
import minicode.com.controller.CoderExercutes.PythonStrat;
import minicode.com.dto.CodeSubmission;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

@RestController
@RequestMapping("/api/code/playground")
public class CodeController {

    private final HashMap<String, CodeExercuteStrat> strategies = new HashMap<>();

    public CodeController() {
        strategies.put("python", new PythonStrat());
        strategies.put("java", new JavaStrat());
        strategies.put("cpp", new CppStrat());
        strategies.put("c++", new CppStrat());
        strategies.put("csharp", new CSharpStrat());
        strategies.put("c#", new CSharpStrat());
    }

    @PostMapping("/submit")
    public String submitCode(@RequestBody CodeSubmission submission) throws IOException {
        String language = submission.language().toLowerCase(Locale.ROOT);
        CodeExercuteStrat strategy = strategies.get(language);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }

        try {
            return strategy.execute(submission.code());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Code execution was interrupted", e);
        }
    }
}
