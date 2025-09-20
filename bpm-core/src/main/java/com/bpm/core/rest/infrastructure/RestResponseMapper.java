package com.bpm.core.rest.infrastructure;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

public class RestResponseMapper {

    private final Context engine;

    public RestResponseMapper(Context engine) {
        this.engine = engine;
    }

    public Object processResponseMapping(String script, String response) {
        if (script == null || script.isEmpty()) return response;
        try {
            // Get the language's global scope and put the 'response' variable into it.
            Value bindings = engine.getBindings("js");
            bindings.putMember("response", response);

            // Evaluate the script.
            Value result = engine.eval("js", script);
            
            // Check if the result is not null and is a valid value.
            if (result != null && !result.isNull()) {
                // Return the result as a Java object.
                return result.as(Object.class);
            }
            
            return response;

        } catch (PolyglotException e) {
            // Handle script execution errors gracefully.
            return response;
        }
    }
}
