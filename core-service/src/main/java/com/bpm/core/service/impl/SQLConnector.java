package com.bpm.core.service.impl;

import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.entity.ServiceLog;
import com.bpm.core.repository.DbConfigRepository;
import com.bpm.core.repository.ServiceLogRepository;
import com.bpm.core.util.DataSourceUtil;
import com.bpm.core.util.TemplateUtil;
import com.bpm.core.cache.DataSourceCache;
import com.bpm.core.constant.SQL_TYPE;
import com.bpm.core.entity.DbConfig;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.util.*;

import javax.sql.DataSource;

@Service
public class SQLConnector {

    private final DbConfigRepository dbConfigRepository;
    private final ServiceLogRepository logRepository;

    public SQLConnector(DbConfigRepository dbConfigRepository, ServiceLogRepository logRepository) {
        this.dbConfigRepository = dbConfigRepository;
        this.logRepository = logRepository;
    }

    public Object invoke(ServiceConfig config, Map<String, Object> params) {
        String sqlType = config.getSqlType();
        String serviceCode = config.getServiceCode();

        try {
            switch (sqlType.toUpperCase()) {
                case SQL_TYPE.QUERY:
                    return executeQuery(config, params);

                case SQL_TYPE.UPDATE:
                    return executeUpdate(config, params);

                case SQL_TYPE.PROCEDURE:
                    return executeProcedure(config, params);

                default:
                    return Collections.singletonMap("error", "Unsupported SQL type: " + sqlType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logExecution(serviceCode, params.toString(), e.getMessage(), 1, 0);
            return Collections.singletonMap("error", "DB invoke error: " + e.getMessage());
        }
    }

    // 1. SELECT
    private List<Map<String, Object>> executeQuery(ServiceConfig config, Map<String, Object> params) {
        long start = System.currentTimeMillis();
        String serviceCode = config.getServiceCode();

        try {
            List<Object> queryParams = TemplateUtil.extractParams(config.getPayloadTemplate(), params);
            JdbcTemplate jdbcTemplate = getJdbcTemplate(config.getDbDatasource());

            List<Map<String, Object>> result = jdbcTemplate.queryForList(config.getSqlStatement(), queryParams.toArray());
            logExecution(serviceCode, queryParams.toString(), result.toString(), 0, System.currentTimeMillis() - start);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            logExecution(serviceCode, params.toString(), e.getMessage(), 1, System.currentTimeMillis() - start);
            return Collections.singletonList(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // 2. INSERT/UPDATE/DELETE
    private Map<String, Integer> executeUpdate(ServiceConfig config, Map<String, Object> params) {
        long start = System.currentTimeMillis();
        String serviceCode = config.getServiceCode();

        try {
            List<Object> updateParams = TemplateUtil.extractParams(config.getPayloadTemplate(), params);
            JdbcTemplate jdbcTemplate = getJdbcTemplate(config.getDbDatasource());

            int rows = jdbcTemplate.update(config.getSqlStatement(), updateParams.toArray());
            Map<String, Integer> result = Collections.singletonMap("updatedRows", rows);

            logExecution(serviceCode, updateParams.toString(), result.toString(), 0, System.currentTimeMillis() - start);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            logExecution(serviceCode, params.toString(), e.getMessage(), 1, System.currentTimeMillis() - start);
            return Collections.singletonMap("error", -1);
        }
    }

    // 3. PROCEDURE
    private Map<String, Object> executeProcedure(ServiceConfig config, Map<String, Object> params) {
        long start = System.currentTimeMillis();
        String serviceCode = config.getServiceCode();

        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate(config.getDbDatasource());

            SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName(config.getSqlStatement());

            Map<String, Object> inParams = TemplateUtil.extractNamedParams(config.getPayloadTemplate(), params);
            Map<String, Object> resultMap = call.execute(inParams);

            List<String> outParamNames = TemplateUtil.extractOutParams(config.getPayloadTemplate());
            Map<String, Object> filteredResult = new LinkedHashMap<>();
            for (String name : outParamNames) {
                filteredResult.put(name, resultMap.get(name));
            }

            logExecution(serviceCode, inParams.toString(), filteredResult.toString(), 0, System.currentTimeMillis() - start);
            return filteredResult;

        } catch (Exception e) {
            e.printStackTrace();
            logExecution(serviceCode, params.toString(), e.getMessage(), 1, System.currentTimeMillis() - start);
            return Collections.singletonMap("error", e.getMessage());
        }
    }

    // Helper: get JdbcTemplate from datasource name
    private JdbcTemplate getJdbcTemplate(String datasourceName) {
        DbConfig dbConfig = dbConfigRepository.findByName(datasourceName);
        if (dbConfig == null) {
            throw new RuntimeException("Datasource not found: " + datasourceName);
        }

        DataSource ds = DataSourceCache.getOrCreate(dbConfig);
        return DataSourceUtil.createJdbcTemplate(ds);
    }

    // Log to service_log table
    private Long logExecution(String serviceCode, String requestData, String responseData, int statusCode, long duration) {
        if (logRepository == null) return null;

        ServiceLog log = new ServiceLog(serviceCode, null, requestData, responseData, statusCode, (int) duration);
        try {
            return logRepository.insertLog(log);
        } catch (Exception e) {
            System.err.println("Failed to insert log: " + e.getMessage());
            return null;
        }
    }
}
