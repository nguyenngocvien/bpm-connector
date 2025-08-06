package com.bpm.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> toStringMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }

    public static Map<String, Object> toObjectMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }

    public static String toString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
    
    public List<Object> extractParams(JsonNode node, String fieldName) {
        List<Object> result = new ArrayList<>();
        JsonNode arr = node.get(fieldName);
        if (arr != null && arr.isArray()) {
            for (JsonNode item : arr) {
                if (item.isNumber()) result.add(item.numberValue());
                else if (item.isBoolean()) result.add(item.booleanValue());
                else result.add(item.asText());
            }
        }
        return result;
    }
    
    public String errorJson(Exception e) {
        String msg = e.getClass().getSimpleName() + ": " + e.getMessage();
        return "{\"error\":\"" + msg.replace("\"", "'") + "\"}";
    }
    
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    public String escapeJson(String s) {
        return s.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    public static String toPrettyString(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}

