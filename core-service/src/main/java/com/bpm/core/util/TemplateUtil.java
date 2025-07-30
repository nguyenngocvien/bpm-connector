package com.bpm.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TemplateUtil {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * Replace ${key} in template with values from params.
     */
    public static String render(String template, Map<String, Object> params) {
        if (template == null) return "";

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = params.getOrDefault(key, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value.toString()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Parse JSON string into Map<String, String> and render templates inside.
     */
    public static Map<String, String> parseJsonToMap(String jsonString, Map<String, Object> params) {
        try {
            if (jsonString == null || jsonString.isEmpty()) return Collections.emptyMap();

            Map<String, String> map = JsonUtil.toStringMap(jsonString);
            map.replaceAll((k, v) -> render(v, params));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();  // fallback to empty map
        }
    }
    
    
//    {
//	  "params": ["${param1}", "${param2}"],
//	  "namedInParams": {"id": "${id}"},
//	  "outParams": ["result", "status"]
//	}

    public static List<Object> extractParams(String templateJson, Map<String, Object> params) {
        try {
            JsonNode node = new ObjectMapper().readTree(templateJson);
            JsonNode paramNodes = node.get("params");
            List<Object> extracted = new ArrayList<>();
            if (paramNodes != null && paramNodes.isArray()) {
                for (JsonNode p : paramNodes) {
                    String rendered = TemplateUtil.render(p.asText(), params);
                    extracted.add(rendered);
                }
            }
            return extracted;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public static Map<String, Object> extractNamedParams(String templateJson, Map<String, Object> params) {
        try {
            JsonNode node = new ObjectMapper().readTree(templateJson);
            JsonNode namedParams = node.get("namedInParams");
            Map<String, Object> map = new LinkedHashMap<>();
            if (namedParams != null && namedParams.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = namedParams.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String rendered = TemplateUtil.render(entry.getValue().asText(), params);
                    map.put(entry.getKey(), rendered);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
    
    public static List<String> extractOutParams(String templateJson) {
        try {
            JsonNode node = new ObjectMapper().readTree(templateJson);
            JsonNode outParams = node.get("outParams");
            List<String> list = new ArrayList<>();
            if (outParams != null && outParams.isArray()) {
                for (JsonNode p : outParams) {
                    list.add(p.asText());
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}