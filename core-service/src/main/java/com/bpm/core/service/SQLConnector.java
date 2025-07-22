package com.bpm.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.bpm.core.entity.SQLRequest;
import com.bpm.core.util.ErrorUtil;
import com.bpm.core.constant.DB_INVOKE_TYPE;
import com.bpm.core.entity.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Types;
import java.util.*;

@Component
public class SQLConnector {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SQLConnector(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    // 1. Execute SELECT
    public String executeQuery(String jsonInput) {
        long start = System.currentTimeMillis();
        int status = 0;
        String responseJson;

        try {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            List<Map<String, Object>> result = jdbcTemplate.queryForList(req.getSql(), req.getParams().toArray());

            Response<List<Map<String, Object>>> res = Response.success(result);
            responseJson = objectMapper.writeValueAsString(res);
            return responseJson;

        } catch (Exception e) {
            status = 1;
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.QUERY, jsonInput, status, start);
        }
    }

    // 2. Execute UPDATE/INSERT/DELETE
    public String executeUpdate(String jsonInput) {
        long start = System.currentTimeMillis();
        int status = 0;
        String responseJson;

        try {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            int rows = jdbcTemplate.update(req.getSql(), req.getParams().toArray());

            Map<String, Integer> result = Collections.singletonMap("updatedRows", rows);
            Response<Map<String, Integer>> res = Response.success(result);
            responseJson = objectMapper.writeValueAsString(res);
            return responseJson;

        } catch (Exception e) {
            status = 1;
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.UPDATE, jsonInput, status, start);
        }
    }

    // 3. Execute PROCEDURE
    public String executeProcedure(String jsonInput) {
        long start = System.currentTimeMillis();
        int status = 0;
        String responseJson;

        try {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            String procName = req.getName();
            List<Object> inParams = req.getInParams();
            List<String> outParams = req.getOutParams();

            SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName(procName);

            Map<String, Object> inMap = new HashMap<>();
            for (int i = 0; i < inParams.size(); i++) {
                inMap.put("in" + (i + 1), inParams.get(i));
            }

            for (String out : outParams) {
                call.declareParameters(new org.springframework.jdbc.core.SqlOutParameter(out, Types.VARCHAR));
            }

            Map<String, Object> out = call.execute(inMap);
            Response<Map<String, Object>> res = Response.success(out);
            responseJson = objectMapper.writeValueAsString(res);
            return responseJson;

        } catch (Exception e) {
            status = 1;
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.PROCEDURE, jsonInput, status, start);
        }
    }
}
