package minicode.com.controller.CoderExercutes.OutputFeedback;

import minicode.com.controller.SupportedLanguages;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Map;

public final class OutputFeedbackFactory {

    private static final Map<SupportedLanguages, OutputFeedbackStrat> STRATEGIES = Map.of(
            SupportedLanguages.PYTHON, new PythonOutputFeedback(),
            SupportedLanguages.JAVA, new JavaOutputFeedback(),
            SupportedLanguages.CPLUSPLUS, new CplusplusOutputFeedback(),
            SupportedLanguages.CSHARP, new CsharpOutputFeedback()
    );

    private OutputFeedbackFactory() {
    }

    public static OutputFeedbackStrat forLanguage(String language) {
        SupportedLanguages key;
        try {
            key = SupportedLanguages.valueOf(language.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported language: " + language
            );
        }
        OutputFeedbackStrat strategy = STRATEGIES.get(key);
        if (strategy == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported language: " + language
            );
        }
        return strategy;
    }
}