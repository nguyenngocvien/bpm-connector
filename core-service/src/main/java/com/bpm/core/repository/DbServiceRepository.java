package com.bpm.core.repository;

import com.bpm.core.model.db.DbOutputMapping;
import com.bpm.core.model.db.DbParamConfig;
import com.bpm.core.model.db.DbServiceConfig;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DbServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DbServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_db ORDER BY id ASC";
        return jdbcTemplate.query(sql, dbServiceRowMapper);
    }

    public Optional<DbServiceConfig> findById(Long id) {
        String sql = "SELECT * FROM core_service_db WHERE id = ?";
        try {
            DbServiceConfig config = jdbcTemplate.queryForObject(sql, dbServiceRowMapper, id);
            if (config != null) {
                config.setParamList(findParamsByConfigId(id));
                config.setOutputMappingList(findOutputMappingsByConfigId(id));
            }
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private final RowMapper<DbServiceConfig> dbServiceRowMapper = (rs, rowNum) -> {
        return DbServiceConfig.builder()
                .id(rs.getLong("id"))
                .dbDatasource(rs.getString("db_datasource"))
                .sqlStatement(rs.getString("sql_statement"))
                .sqlType(rs.getString("sql_type"))
                .inputParams(rs.getString("input_params"))
                .outputMapping(rs.getString("output_mapping"))
                .timeoutMs(rs.getInt("timeout_ms"))
                .retryCount(rs.getInt("retry_count"))
                .retryBackoffMs(rs.getInt("retry_backoff_ms"))
                .transactional(rs.getBoolean("transactional"))
                .fetchSize(rs.getInt("fetch_size"))
                .resultType(rs.getString("result_type"))
                .enabled(rs.getBoolean("enabled"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    };

    private List<DbParamConfig> findParamsByConfigId(Long configId) {
        String sql = "SELECT * FROM core_service_db_param WHERE db_config_id = ? ORDER BY param_order ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapDbParam(rs), configId);
    }

    private DbParamConfig mapDbParam(ResultSet rs) throws SQLException {
        DbParamConfig param = new DbParamConfig();
        param.setId(rs.getInt("id"));
        param.setDbConfigId(rs.getLong("db_config_id"));
        param.setParamName(rs.getString("param_name"));
        param.setParamType(rs.getString("param_type"));
        param.setParamMode(rs.getString("param_mode"));
        param.setParamOrder(rs.getInt("param_order"));
        return param;
    }

    private List<DbOutputMapping> findOutputMappingsByConfigId(Long configId) {
        String sql = "SELECT * FROM core_service_db_output_map WHERE db_config_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapDbOutput(rs), configId);
    }

    private DbOutputMapping mapDbOutput(ResultSet rs) throws SQLException {
        DbOutputMapping output = new DbOutputMapping();
        output.setId(rs.getInt("id"));
        output.setDbConfigId(rs.getLong("db_config_id"));
        output.setColumnName(rs.getString("column_name"));
        output.setOutputField(rs.getString("output_field"));
        return output;
    }

    public int save(DbServiceConfig config) {
        String sql = ""
                + "INSERT INTO core_service_db (id, db_datasource, sql_statement, sql_type, input_params, output_mapping, "
                + "timeout_ms, retry_count, retry_backoff_ms, transactional, fetch_size, result_type, enabled, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) "
                + "ON CONFLICT (id) DO UPDATE SET "
                + "db_datasource = EXCLUDED.db_datasource, sql_statement = EXCLUDED.sql_statement, sql_type = EXCLUDED.sql_type, "
                + "input_params = EXCLUDED.input_params, output_mapping = EXCLUDED.output_mapping, timeout_ms = EXCLUDED.timeout_ms, "
                + "retry_count = EXCLUDED.retry_count, retry_backoff_ms = EXCLUDED.retry_backoff_ms, transactional = EXCLUDED.transactional, "
                + "fetch_size = EXCLUDED.fetch_size, result_type = EXCLUDED.result_type, enabled = EXCLUDED.enabled, updated_at = CURRENT_TIMESTAMP";

        return jdbcTemplate.update(sql,
                config.getId(),
                config.getDbDatasource(),
                config.getSqlStatement(),
                config.getSqlType(),
                config.getInputParams(),
                config.getOutputMapping(),
                config.getTimeoutMs(),
                config.getRetryCount(),
                config.getRetryBackoffMs(),
                config.getTransactional(),
                config.getFetchSize(),
                config.getResultType(),
                config.getEnabled()
        );
    }
    
    public int deleteById(Long id) {
        String sql = "DELETE FROM core_service_db WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
