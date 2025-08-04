package com.bpm.core.util;

import com.bpm.core.model.db.DbParamConfig;
import com.bpm.core.model.db.DbOutputMapping;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

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
}
