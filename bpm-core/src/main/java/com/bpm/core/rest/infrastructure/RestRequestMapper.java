package com.bpm.core.rest.infrastructure;

import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.util.Map;

public class RestRequestMapper {

    private final ScriptEngine engine;

    public RestRequestMapper(ScriptEngine engine) {
        this.engine = engine;
    }

    public String processRequestMapping(String script, Map<String, Object> params) {
        if (script == null || script.isEmpty()) return "";
        try {
            SimpleBindings bindings = new SimpleBindings();
            bindings.put("input", params);
            Object result = engine.eval(script, bindings);
            return (result != null) ? result.toString() : "";
        } catch (Exception e) {
            throw new RuntimeException("Error in request mapping script: " + e.getMessage(), e);
        }
    }
}
