package com.bpm.core.rest.infrastructure;

import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;

public class RestResponseMapper {

    private final ScriptEngine engine;

    public RestResponseMapper(ScriptEngine engine) {
        this.engine = engine;
    }

    public Object processResponseMapping(String script, String response) {
        if (script == null || script.isEmpty()) return response;
        try {
            Map<String, Object> context = new HashMap<>();
            context.put("response", response);
            SimpleBindings bindings = new SimpleBindings(context);
            Object result = engine.eval(script, bindings);
            return result != null ? result : response;
        } catch (Exception e) {
            return response;
        }
    }
}
