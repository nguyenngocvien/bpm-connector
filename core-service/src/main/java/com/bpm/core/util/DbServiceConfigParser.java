package com.bpm.core.util;

import com.bpm.core.model.db.DbParamConfig;
import com.bpm.core.model.db.ParamType;
import com.bpm.core.model.db.DbOutputMapping;
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
            ParamType type = param.getParamType();

            Object defaultValue = null; // mặc định

            if (type != null) {
                switch (type) {
                    case VARCHAR:
                    case TEXT:
                        defaultValue = "";
                        break;

                    case INTEGER:
                    case BIGINT:
                    case SMALLINT:
                    case NUMERIC:
                    case DECIMAL:
                    case DOUBLE:
                    case REAL:
                        defaultValue = null;
                        break;

                    case BOOLEAN:
                        defaultValue = false;
                        break;

                    case DATE:
                    case TIMESTAMP:
                        defaultValue = null; // có thể set LocalDate.now() / LocalDateTime.now()
                        break;

                    case BYTEA:
                        defaultValue = null; // có thể để [] nếu muốn array rỗng
                        break;

                    default:
                        defaultValue = null;
                }
            }

            result.put(name, defaultValue);
        }

        return JsonUtil.toPrettyString(result);
    }
}
