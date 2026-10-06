package minicode.com.controller.CodeEnv.Submissionfile;

import minicode.com.controller.SupportedLanguages;
import minicode.com.exception.UnSupportedLanguage;

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
        SupportedLanguages key;
        try {
            key = SupportedLanguages.valueOf(language.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw UnSupportedLanguage.unsupportedLanguage(language);
        }
        SubmissionFileName strategy = STRATEGIES.get(key);
        if (strategy == null) {
            throw UnSupportedLanguage.unsupportedLanguage(language);
        }
        return strategy;
    }
}
