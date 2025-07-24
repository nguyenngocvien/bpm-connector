package com.bpm.core.service;

import com.bpm.core.entity.SQLRequest;
import com.bpm.core.util.ErrorUtil;
import com.bpm.core.constant.DB_INVOKE_TYPE;
import com.bpm.core.entity.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SQLConnector {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public SQLConnector(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    // 1. SELECT
    public String executeQuery(String jsonInput) {
        long start = System.currentTimeMillis();
        try {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            List<Map<String, Object>> result = jdbcTemplate.queryForList(req.getSql(), req.getParams().toArray());
            Response<List<Map<String, Object>>> res = Response.success(result);
            return objectMapper.writeValueAsString(res);
        } catch (Exception e) {
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.QUERY, jsonInput, 1, start);
        }
    }

    // 2. INSERT/UPDATE/DELETE
    public String executeUpdate(String jsonInput) {
        long start = System.currentTimeMillis();
        try {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            int rows = jdbcTemplate.update(req.getSql(), req.getParams().toArray());
            Map<String, Integer> result = Collections.singletonMap("updatedRows", rows);
            Response<Map<String, Integer>> res = Response.success(result);
            return objectMapper.writeValueAsString(res);
        } catch (Exception e) {
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.UPDATE, jsonInput, 1, start);
        }
    }

    // 3. PROCEDURE
    public String executeProcedure(String jsonInput) {
        long start = System.currentTimeMillis();
        try {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName(req.getName());

            Map<String, Object> inParamMap = buildInParamMap(req.getInParams());
            Map<String, Object> resultMap = call.execute(inParamMap);

            Map<String, Object> filteredResult = new LinkedHashMap<>();
            for (String name : req.getOutParams()) {
                filteredResult.put(name, resultMap.get(name));
            }

            Response<Map<String, Object>> res = Response.success(filteredResult);
            return objectMapper.writeValueAsString(res);
        } catch (Exception e) {
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.PROCEDURE, jsonInput, 1, start);
        }
    }

    // Helper: convert input params to Map<String, Object> for procedure
    private Map<String, Object> buildInParamMap(List<Object> inParams) {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        for (int i = 0; i < inParams.size(); i++) {
            paramMap.put("in" + (i + 1), inParams.get(i));
        }
        return paramMap;
    }
}
