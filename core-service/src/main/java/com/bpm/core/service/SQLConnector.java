package com.bpm.core.service;

import com.bpm.core.entity.SQLRequest;
import com.bpm.core.util.ErrorUtil;
import com.bpm.core.constant.DB_INVOKE_TYPE;
import com.bpm.core.entity.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import javax.sql.DataSource;
public class SQLConnector {

    private final DataSource dataSource;
    private final ObjectMapper objectMapper;

    public SQLConnector(DataSource dataSource, ObjectMapper objectMapper) {
        this.dataSource = dataSource;
        this.objectMapper = objectMapper;
    }

    // 1. SELECT
    public String executeQuery(String jsonInput) {
        long start = System.currentTimeMillis();
        try (Connection conn = dataSource.getConnection()) {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            try (PreparedStatement stmt = conn.prepareStatement(req.getSql())) {
                setParameters(stmt, req.getParams());
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Map<String, Object>> result = mapResultSet(rs);
                    Response<List<Map<String, Object>>> res = Response.success(result);
                    return objectMapper.writeValueAsString(res);
                }
            }
        } catch (Exception e) {
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.QUERY, jsonInput, 1, start);
        }
    }

    // 2. INSERT/UPDATE/DELETE
    public String executeUpdate(String jsonInput) {
        long start = System.currentTimeMillis();
        try (Connection conn = dataSource.getConnection()) {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            try (PreparedStatement stmt = conn.prepareStatement(req.getSql())) {
                setParameters(stmt, req.getParams());
                int rows = stmt.executeUpdate();
                Map<String, Integer> result = Collections.singletonMap("updatedRows", rows);
                Response<Map<String, Integer>> res = Response.success(result);
                return objectMapper.writeValueAsString(res);
            }
        } catch (Exception e) {
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.UPDATE, jsonInput, 1, start);
        }
    }

    // 3. PROCEDURE
    public String executeProcedure(String jsonInput) {
        long start = System.currentTimeMillis();
        try (Connection conn = dataSource.getConnection()) {
            SQLRequest req = objectMapper.readValue(jsonInput, SQLRequest.class);
            String procCall = buildProcedureCall(req.getName(), req.getInParams(), req.getOutParams().size());

            try (CallableStatement stmt = conn.prepareCall(procCall)) {
                setInParams(stmt, req.getInParams());
                registerOutParams(stmt, req.getOutParams());
                stmt.execute();

                Map<String, Object> result = new HashMap<>();
                for (int i = 0; i < req.getOutParams().size(); i++) {
                    String name = req.getOutParams().get(i);
                    result.put(name, stmt.getObject(req.getInParams().size() + i + 1));
                }

                Response<Map<String, Object>> res = Response.success(result);
                return objectMapper.writeValueAsString(res);
            }
        } catch (Exception e) {
            return ErrorUtil.generateErrorJson(e.getMessage(), DB_INVOKE_TYPE.PROCEDURE, jsonInput, 1, start);
        }
    }

    // Helper: Set parameters to PreparedStatement
    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }
    }

    // Helper: Map ResultSet to List<Map<String, Object>>
    private List<Map<String, Object>> mapResultSet(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= colCount; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            result.add(row);
        }
        return result;
    }

    // Helper: CallableStatement for procedure call
    private String buildProcedureCall(String name, List<Object> inParams, int outCount) {
        int total = inParams.size() + outCount;
        StringBuilder sb = new StringBuilder("{call ");
        sb.append(name).append("(");
        for (int i = 0; i < total; i++) {
            sb.append("?,");
        }
        if (total > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append(")}");
        return sb.toString();
    }

    private void setInParams(CallableStatement stmt, List<Object> inParams) throws SQLException {
        for (int i = 0; i < inParams.size(); i++) {
            stmt.setObject(i + 1, inParams.get(i));
        }
    }

    private void registerOutParams(CallableStatement stmt, List<String> outParams) throws SQLException {
        for (int i = 0; i < outParams.size(); i++) {
            stmt.registerOutParameter(inParamsCount() + i + 1, Types.VARCHAR); // default as VARCHAR
        }
    }

    private int inParamsCount() {
        return 0; // You'll want to adjust this depending on context
    }
}