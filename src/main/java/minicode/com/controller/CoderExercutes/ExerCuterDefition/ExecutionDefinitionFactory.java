package minicode.com.controller.CoderExercutes.ExerCuterDefition;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
