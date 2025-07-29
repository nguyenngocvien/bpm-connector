package com.bpm.core.service;

import com.bpm.core.entity.SQLRequest;
import com.bpm.core.entity.ServiceLog;
import com.bpm.core.repository.DbConfigRepository;
import com.bpm.core.repository.ServiceLogRepository;
import com.bpm.core.util.DataSourceUtil;
import com.bpm.core.cache.DataSourceCache;
import com.bpm.core.config.DbConfig;
import com.bpm.core.constant.DB_INVOKE_TYPE;
import com.bpm.core.entity.Response;
import com.bpm.core.entity.SQLProcedureRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.util.*;

import javax.sql.DataSource;

@Service
public class SQLConnector {

    private final DbConfigRepository configRepository;
    private final ServiceLogRepository logRepository;
    
    public SQLConnector(DbConfigRepository configRepository, ServiceLogRepository logRepository) {
        this.configRepository = configRepository;
        this.logRepository = logRepository;
    }

    // 1. SELECT
    public Response<List<Map<String, Object>>> executeQuery(SQLRequest req) {
        long start = System.currentTimeMillis();
        try {
        	JdbcTemplate jdbcTemplate = getJdbcTemplate(req.getDatasource());

            List<Map<String, Object>> result = jdbcTemplate.queryForList(req.getSql(), req.getParams().toArray());
            Response<List<Map<String, Object>>> res = Response.success(result);
            
            return res;
            
        } catch (Exception e) {
        	e.printStackTrace(); // or use logger.error("...", e)
        	Long id = logExecution(DB_INVOKE_TYPE.QUERY, req.toString(), e.getMessage(), 1, start);
        	return Response.error(1, e.getMessage(), id);
        }
    }

    // 2. INSERT/UPDATE/DELETE
    public Response<Map<String, Integer>> executeUpdate(SQLRequest req) {
        long start = System.currentTimeMillis();
        try {
        	JdbcTemplate jdbcTemplate = getJdbcTemplate(req.getDatasource());

            int rows = jdbcTemplate.update(req.getSql(), req.getParams().toArray());
            Map<String, Integer> result = Collections.singletonMap("updatedRows", rows);
            Response<Map<String, Integer>> res = Response.success(result);
            
            return res;
            
        } catch (Exception e) {
        	e.printStackTrace(); // or use logger.error("...", e)
        	Long id = logExecution(DB_INVOKE_TYPE.UPDATE, req.toString(), e.getMessage(), 1, start);
        	return Response.error(1, e.getMessage(), id);
        }
    }

    // 3. PROCEDURE
    public Response<Map<String, Object>> executeProcedure(SQLProcedureRequest req) {
        long start = System.currentTimeMillis();
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate(req.getDatasource());

            SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName(req.getName());

            // Convert in params
            Map<String, Object> inParamMap = req.getNamedInParams() != null ?
                    new LinkedHashMap<>(req.getNamedInParams()) :
                    Collections.emptyMap();

            // Execute stored procedure
            Map<String, Object> resultMap = call.execute(inParamMap);

            // Filter result by outParams
            Map<String, Object> filteredResult = new LinkedHashMap<>();
            if (resultMap != null && req.getOutParams() != null) {
                for (String name : req.getOutParams()) {
                    filteredResult.put(name, resultMap.get(name));
                }
            }

            long duration = System.currentTimeMillis() - start;
            Response<Map<String, Object>> res = Response.success(filteredResult);
            res.setLogId(logExecution(DB_INVOKE_TYPE.PROCEDURE, req.toString(), res.toString(), 0, duration));
            return res;

        } catch (Exception e) {
            e.printStackTrace();  // For debug - can replace with proper logger
            long duration = System.currentTimeMillis() - start;
            Long logId = logExecution(DB_INVOKE_TYPE.PROCEDURE, req.toString(), e.getMessage(), 1, duration);
            return Response.error(1, e.getMessage(), logId);
        }
    }

    // Helper: convert input params to Map<String, Object> for procedure    
    private JdbcTemplate getJdbcTemplate(String datasourceName) {
        // 1. Load config
        DbConfig config = configRepository.findByName(datasourceName);
        
        // 2. Cache DataSource by name
        DataSource ds = DataSourceCache.getOrCreate(datasourceName, config);

        return DataSourceUtil.createJdbcTemplate(ds);
    }


    private Long logExecution(String serviceCode, String requestData, String responseData, int statusCode, long duration) {
        if (logRepository == null) return null;

        ServiceLog log = new ServiceLog(
            serviceCode,
            null,
            requestData,
            responseData,
            statusCode,
            (int) duration
        );
        try {
            return logRepository.insertLog(log);
        } catch (Exception e) {
            System.err.println("Failed to insert log: " + e.getMessage());
            return null;
        }
    }
}
