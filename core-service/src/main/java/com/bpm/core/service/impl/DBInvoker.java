package com.bpm.core.service.impl;

import com.bpm.core.cache.DataSourceCache;
import com.bpm.core.model.db.DataSourceConfig;
import com.bpm.core.model.db.DbOutputMapping;
import com.bpm.core.model.db.DbParamConfig;
import com.bpm.core.model.db.DbServiceConfig;
import com.bpm.core.model.log.ServiceLog;
import com.bpm.core.model.response.Response;
import com.bpm.core.model.service.ServiceConfig;
import com.bpm.core.repository.DataSourceRepository;
import com.bpm.core.repository.DbServiceRepository;
import com.bpm.core.repository.ServiceLogRepository;
import com.bpm.core.service.ServiceInvoker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.*;

@Service("dbInvoker")
public class DBInvoker implements ServiceInvoker {

    @Autowired
    private DbServiceRepository repository;
    
    @Autowired
    private DataSourceRepository dsRepository;

    @Autowired
    private ServiceLogRepository logRepository;

    @Autowired
    private PlatformTransactionManager txManager;
    
    private static final int DEFAULT_TIMEOUT_SECONDS = 3;
    private static final int DEFAULT_FETCH_SIZE = 100;

    @Override
    public Response<Object> invoke(ServiceConfig serviceConfig, Map<String, Object> inputParams) {
        DbServiceConfig config = repository.findById(serviceConfig.getId())
                .orElseThrow(() -> new RuntimeException("DB config not found for ID: " + serviceConfig.getId()));
        
        DataSourceConfig ds = dsRepository.findByName(config.getDbDatasource())
                .orElseThrow(() -> new RuntimeException("Data source not found for name: " + config.getDbDatasource()));

        DataSource dataSource = DataSourceCache.getOrCreate(ds);
        if (dataSource == null) {
            return Response.error("Datasource not found: " + config.getDbDatasource());
        }

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(config.getFetchSize() != null ? config.getFetchSize() : DEFAULT_FETCH_SIZE);
        jdbcTemplate.setQueryTimeout(config.getTimeoutMs() != null ? config.getTimeoutMs() / 1000 : DEFAULT_TIMEOUT_SECONDS);

        String sql = config.getSqlStatement();
        String sqlType = config.getSqlType().toUpperCase();
        boolean transactional = Boolean.TRUE.equals(config.getTransactional());
        String resultType = config.getResultType() != null ? config.getResultType() : "LIST";

        long start = System.currentTimeMillis();
        Object resultData = null;
        int statusCode = 200;
        String errorMsg = null;
        Long logId = null;

        try {
            TransactionTemplate txTemplate = new TransactionTemplate(txManager);
            if (!transactional) txTemplate.setReadOnly(true);

            resultData = txTemplate.execute(status -> {
                try {
                    if ("QUERY".equals(sqlType)) {
                        return executeQuery(jdbcTemplate, sql, config.getParamList(), inputParams, resultType, config.getOutputMappingList());
                    } else if ("UPDATE".equals(sqlType)) {
                        int updateCount = executeUpdate(jdbcTemplate, sql, config.getParamList(), inputParams);
                        return Collections.singletonMap("rowsAffected", updateCount);
                    } else if ("PROC".equals(sqlType)) {
                        return executeStoredProc(jdbcTemplate, sql, config.getParamList(), inputParams, config.getOutputMappingList());
                    } else {
                        throw new UnsupportedOperationException("Unknown sqlType: " + sqlType);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Execution failed: " + e.getMessage(), e);
                }
            });

        } catch (Exception ex) {
            statusCode = 500;
            errorMsg = ex.getMessage();
            resultData = errorMsg;
        } finally {
            if (Boolean.TRUE.equals(serviceConfig.getLogEnabled())) {
            	ServiceLog log = new ServiceLog();
            	log.setServiceCode(serviceConfig.getServiceCode());
            	log.setRequestData(inputParams.toString());
            	log.setMappedRequest(sql);
            	log.setResponseData(resultData != null ? resultData.toString() : null);
            	log.setStatusCode(statusCode);
            	log.setDurationMs((int) (System.currentTimeMillis() - start));
            	try {
            	    logId = logRepository.insertLog(log);
            	} catch (Exception e) {
            	    System.err.println("Failed to insert log: " + e.getMessage());
            	}
            }
        }

        if (statusCode == 200) {
            return Response.success(resultData);
        } else {
            String msg = errorMsg + (logId != null ? " [Log_ID: " + logId + "]" : "");
            return Response.error(msg);
        }
    }

    private Object executeQuery(JdbcTemplate jdbcTemplate, String sql,
                                List<DbParamConfig> paramList,
                                Map<String, Object> inputParams,
                                String resultType,
                                List<DbOutputMapping> outputMap) {

        Object[] paramValues = getParamValues(paramList, inputParams);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, paramValues);

        if ("LIST".equalsIgnoreCase(resultType)) {
            return mapOutput(rows, outputMap);
        } else if ("SINGLE".equalsIgnoreCase(resultType)) {
            return rows.isEmpty() ? null : mapOutput(rows.get(0), outputMap);
        } else {
            return null;
        }
    }

    private int executeUpdate(JdbcTemplate jdbcTemplate, String sql,
                              List<DbParamConfig> paramList,
                              Map<String, Object> inputParams) {

        Object[] paramValues = getParamValues(paramList, inputParams);
        return jdbcTemplate.update(sql, paramValues);
    }

    private Map<String, Object> executeStoredProc(JdbcTemplate jdbcTemplate, String procName,
                                                  List<DbParamConfig> paramList,
                                                  Map<String, Object> inputParams,
                                                  List<DbOutputMapping> outputMappingList) {

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName(procName);
        Map<String, Object> inParams = new HashMap<>();
        Map<String, SqlParameter> sqlParams = new HashMap<>();

        for (DbParamConfig param : paramList) {
            String name = param.getParamName();
            int sqlType = toSqlType(param.getParamType());
            if ("IN".equalsIgnoreCase(param.getParamMode())) {
                inParams.put(name, inputParams.get(name));
                sqlParams.put(name, new SqlParameter(name, sqlType));
            } else if ("OUT".equalsIgnoreCase(param.getParamMode())) {
                sqlParams.put(name, new SqlOutParameter(name, sqlType));
            }
        }

        call.declareParameters(sqlParams.values().toArray(new SqlParameter[0]));
        Map<String, Object> result = call.execute(inParams);
        return mapOutput(result, outputMappingList);
    }

    private Object[] getParamValues(List<DbParamConfig> paramList, Map<String, Object> inputParams) {
        if (paramList == null || paramList.isEmpty()) return new Object[0];
        return paramList.stream()
                .sorted(Comparator.comparingInt(DbParamConfig::getParamOrder))
                .map(p -> inputParams.getOrDefault(p.getParamName(), null))
                .toArray();
    }

    private Map<String, Object> mapOutput(Map<String, Object> row, List<DbOutputMapping> mappingList) {
        if (mappingList == null || mappingList.isEmpty()) return row;
        Map<String, Object> mapped = new HashMap<>();
        for (DbOutputMapping m : mappingList) {
            mapped.put(m.getOutputField(), row.get(m.getColumnName()));
        }
        return mapped;
    }

    private List<Map<String, Object>> mapOutput(List<Map<String, Object>> rows, List<DbOutputMapping> mappingList) {
        if (mappingList == null || mappingList.isEmpty()) return rows;
        List<Map<String, Object>> mappedList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            mappedList.add(mapOutput(row, mappingList));
        }
        return mappedList;
    }

    private int toSqlType(String type) {
        switch (type.toUpperCase()) {
            case "STRING": return Types.VARCHAR;
            case "INT": return Types.INTEGER;
            case "LONG": return Types.BIGINT;
            case "DATE": return Types.DATE;
            case "TIMESTAMP": return Types.TIMESTAMP;
            case "BOOLEAN": return Types.BOOLEAN;
            case "DOUBLE": return Types.DOUBLE;
            default: return Types.VARCHAR;
        }
    }
}