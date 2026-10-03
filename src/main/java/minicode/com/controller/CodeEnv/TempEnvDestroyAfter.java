package minicode.com.controller.CodeEnv;

public class TempEnvDestroyAfter implements EnvStrat {
    private final String language;
    private final String code;
    private final String runner;

    public TempEnvDestroyAfter(String language, String code, String runner) {
        this.language = language;
        this.code = code;
        this.runner = runner;
    }
}
