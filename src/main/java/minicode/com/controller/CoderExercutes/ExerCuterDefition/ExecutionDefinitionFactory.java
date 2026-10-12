package minicode.com.controller.CoderExercutes.ExerCuterDefition;

import java.util.List;

public final class ExecutionDefinitionFactory {

    private static final List<ExecutionDefinition> DEFINITIONS = List.of(
            new PythonExecutionDefinition(),
            new JavaExecutionDefinition(),
            new CplusplusExecutionDefinition(),
            new CsharpExecutionDefinition()
    );


    public static List<ExecutionDefinition> definitions() {
        return DEFINITIONS;
    }

}
