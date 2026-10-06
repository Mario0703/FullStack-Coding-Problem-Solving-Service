package minicode.com.controller.CoderExercutes.OutputFeedback;

import minicode.com.controller.SupportedLanguages;
import minicode.com.exception.UnSupportedLanguage;

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
            throw UnSupportedLanguage.unsupportedLanguage(language);
        }
        OutputFeedbackStrat strategy = STRATEGIES.get(key);
        if (strategy == null) {
            throw UnSupportedLanguage.unsupportedLanguage(language);
        }
        return strategy;
    }
}