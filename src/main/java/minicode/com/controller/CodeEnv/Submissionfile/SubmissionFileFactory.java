package minicode.com.controller.CodeEnv.Submissionfile;

import minicode.com.controller.SupportedLanguages;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public final class SubmissionFileFactory {

    private static final Map<SupportedLanguages, SubmissionFileName> STRATEGIES = Map.of(
            SupportedLanguages.PYTHON, new PythonSubmissionFile(),
            SupportedLanguages.JAVA, new JavaSubmissionFile(),
            SupportedLanguages.CPLUSPLUS, new CplusplusSubmissionFile(),
            SupportedLanguages.CSHARP, new CsharpSubmissionFile()
    );

    private SubmissionFileFactory() {
    }

    public static SubmissionFileName forLanguage(String language) {
        SubmissionFileName strategy = STRATEGIES.get(language);
        if (strategy == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported language: " + language
            );
        }
        return strategy;
    }
}
