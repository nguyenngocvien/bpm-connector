package com.bpm.core.db.infrastructure;

import com.bpm.core.common.util.JsonUtil;
import com.bpm.core.db.domain.DbOutputMapping;
import com.bpm.core.db.domain.DbParamConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbServiceConfigParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    // Parse inputParams JSON -> List<DbParamConfig>
    public static List<DbParamConfig> parseInputParams(String json) {
        try {
            if (json == null || json.isEmpty()) return Collections.emptyList();
            return mapper.readValue(json, new TypeReference<List<DbParamConfig>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Parse outputMapping JSON -> List<DbOutputMapping>
    public static List<DbOutputMapping> parseOutputMapping(String json) {
        try {
            if (json == null || json.isEmpty()) return Collections.emptyList();
            return mapper.readValue(json, new TypeReference<List<DbOutputMapping>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Serialize List<DbParamConfig> -> JSON String
    public static String toInputParamsJson(List<DbParamConfig> params) {
        try {
            return mapper.writeValueAsString(params);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    // Serialize List<DbOutputMapping> -> JSON String
    public static String toOutputMappingJson(List<DbOutputMapping> mappings) {
        try {
            return mapper.writeValueAsString(mappings);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }
    
    public static String convertParamArrayToJsonObject(List<DbParamConfig> paramList) {
    	Map<String, Object> result = new LinkedHashMap<>();
    	for (DbParamConfig param : paramList) {
            String name = param.getParamName();
            String type = param.getParamType().toUpperCase();

            Object defaultValue;
            switch (type) {
                case "VARCHAR":
                case "TEXT":
                case "STRING":
                    defaultValue = "";
                    break;
                case "INT":
                case "INTEGER":
                case "LONG":
                case "FLOAT":
                case "DOUBLE":
                case "DECIMAL":
                    defaultValue = null;
                    break;
                case "BOOLEAN":
                case "BOOL":
                    defaultValue = false;
                    break;
                default:
                    defaultValue = null;
            }

            result.put(name, defaultValue);
        }

        return JsonUtil.toPrettyString(result);
    }
}
