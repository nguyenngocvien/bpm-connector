package com.bpm.core.db.infrastructure;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.bpm.core.common.exception.ServiceExecutionException;
import com.bpm.core.db.cache.DataSourceCache;
import com.bpm.core.db.domain.DbOutputMapping;
import com.bpm.core.db.domain.DbParamConfig;
import com.bpm.core.db.domain.DbServiceConfig;
import com.bpm.core.db.domain.ParamType;
import com.bpm.core.db.domain.SqlType;
import com.bpm.core.db.service.DataSourceConfigService;
import com.bpm.core.db.service.DbServiceConfigService;
import com.bpm.core.serviceconfig.domain.ServiceConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbExecutorHelper {
    private final DbServiceConfigService dbService;
    private final DataSourceConfigService dataSourceService;
    private static final int DEFAULT_TIMEOUT_SECONDS = 3;
    private static final int DEFAULT_FETCH_SIZE = 100;

    public DbExecutorHelper(DbServiceConfigService dbService, DataSourceConfigService dataSourceService) {
        this.dbService = dbService;
        this.dataSourceService = dataSourceService;
    }

    public Object execute(ServiceConfig config, Map<String, Object> inputParams) {
        DbServiceConfig dbConfig = dbService.getConfigById(config.getId());
        if (dbConfig == null) {
            throw new ServiceExecutionException(
                    HttpStatus.NOT_FOUND.value(),
                    "NOT_FOUND",
                    "DbServiceConfig not found for serviceId: " + config.getId(),
                    null
            );
        }

        DataSource dataSource = DataSourceCache.getOrCreate(
                dataSourceService.getDataSourceById(dbConfig.getDbSourceId())
        );
        if (dataSource == null) {
            throw new ServiceExecutionException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "DATASOURCE_ERROR",
                    "Datasource not found: " + dbConfig.getDbSourceId(),
                    null
            );
        }

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(dbConfig.getFetchSize() != null ? dbConfig.getFetchSize() : DEFAULT_FETCH_SIZE);
        jdbcTemplate.setQueryTimeout(dbConfig.getTimeoutMs() != null ? dbConfig.getTimeoutMs() / 1000 : DEFAULT_TIMEOUT_SECONDS);

        String sql = dbConfig.getSqlStatement();
        SqlType sqlType = dbConfig.getSqlType();
        String resultType = dbConfig.getResultType() != null ? dbConfig.getResultType() : "LIST";

        switch (sqlType) {
            case QUERY:
                return executeQuery(jdbcTemplate, sql, dbConfig.getParamList(), inputParams, resultType, dbConfig.getOutputMappingList());
            case UPDATE:
                int updateCount = executeUpdate(jdbcTemplate, sql, dbConfig.getParamList(), inputParams);
                return Collections.singletonMap("rowsAffected", updateCount);
            case STORED_PROC:
                return executeStoredProc(jdbcTemplate, sql, dbConfig.getParamList(), inputParams, dbConfig.getOutputMappingList());
            default:
                throw new UnsupportedOperationException("Unknown sqlType: " + sqlType);
        }
    }

    private Object executeQuery(JdbcTemplate jdbcTemplate, String sql,
                                List<DbParamConfig> paramList,
                                Map<String, Object> inputParams,
                                String resultType,
                                List<DbOutputMapping> outputMap) {
        Object[] paramValues = getParamValues(paramList, inputParams);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, paramValues);
        if ("SINGLE".equalsIgnoreCase(resultType)) {
            return rows.isEmpty() ? null : mapOutput(rows.get(0), outputMap);
        }
        return mapOutput(rows, outputMap);
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
            int sqlType = ParamType.fromString(param.getParamType()).getSqlType();
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
        return rows.stream()
                .map(r -> mapOutput(r, mappingList))
                .collect(Collectors.toList());
    }
}
