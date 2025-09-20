package com.bpm.core.rest.infrastructure;

import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

public class RestRequestMapper {

    private final Context engine;

    public RestRequestMapper(Context engine) {
        this.engine = engine;
    }

    public String processRequestMapping(String script, Map<String, Object> params) {
        if (script == null || script.isEmpty()) return "";
        try {
            // Create a scope for the script execution
            Value bindings = engine.getBindings("js"); // Get the global scope for the language
            bindings.putMember("input", params); // Expose the 'params' map to the script as 'input'

            Value result = engine.eval("js", script); // Evaluate the script
            
            // Check if the result is a valid value
            if (result != null && !result.isNull()) {
                // Convert the GraalVM Value to a Java String
                return result.asString(); 
            }
            
            return "";

        } catch (PolyglotException e) {
            // Catch and handle specific GraalVM exceptions
            throw new RuntimeException("Error in request mapping script: " + e.getMessage(), e);
        }
    }
}
