package com.bpm.core.repository;

import com.bpm.core.entity.ServiceConfig;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. Find All
    public List<ServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_config ORDER BY id ASC";
        return jdbcTemplate.query(sql, serviceConfigRowMapper);
    }

    // 2. Find by ID
    public Optional<ServiceConfig> findById(Long id) {
        String sql = "SELECT * FROM core_service_config WHERE id = ?";
        try {
            ServiceConfig config = jdbcTemplate.queryForObject(sql, serviceConfigRowMapper, id);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 3. Find by Service Code
    public Optional<ServiceConfig> findServiceByCode(String serviceCode) {
        String sql = "SELECT * FROM core_service_config WHERE service_code = ? AND active = true";
        try {
            ServiceConfig config = jdbcTemplate.queryForObject(sql, serviceConfigRowMapper, serviceCode);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 4. Save (Insert or Update)
    public int save(ServiceConfig config) {
        if (config.getId() == null) {
            // INSERT
            String sql = "INSERT INTO core_service_config (service_code, service_type, target_url, http_method, headers, payload_template, db_datasource, sql_statement, sql_type, log_enabled, active, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(sql,
                    config.getServiceCode(),
                    config.getServiceType(),
                    config.getTargetUrl(),
                    config.getHttpMethod(),
                    config.getHeaders(),
                    config.getPayloadTemplate(),
                    config.getDbDatasource(),
                    config.getSqlStatement(),
                    config.getSqlType(),
                    config.getLogEnabled(),
                    config.getActive(),
                    config.getVersion()
            );
        } else {
            // UPDATE
            String sql = "UPDATE core_service_config SET service_code = ?, service_type = ?, target_url = ?, http_method = ?, headers = ?, payload_template = ?, db_datasource = ?, sql_statement = ?, sql_type = ?, log_enabled = ?, active = ?, version = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

            return jdbcTemplate.update(sql,
                    config.getServiceCode(),
                    config.getServiceType(),
                    config.getTargetUrl(),
                    config.getHttpMethod(),
                    config.getHeaders(),
                    config.getPayloadTemplate(),
                    config.getDbDatasource(),
                    config.getSqlStatement(),
                    config.getSqlType(),
                    config.getLogEnabled(),
                    config.getActive(),
                    config.getVersion(),
                    config.getId()
            );
        }
    }

    // 5. Delete by ID
    public int deleteById(Long id) {
        String sql = "DELETE FROM core_service_config WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // RowMapper for ServiceConfig
    private final RowMapper<ServiceConfig> serviceConfigRowMapper = (rs, rowNum) -> {
        ServiceConfig config = new ServiceConfig();
        config.setId(rs.getLong("id"));
        config.setServiceCode(rs.getString("service_code"));
        config.setServiceType(rs.getString("service_type"));
        config.setTargetUrl(rs.getString("target_url"));
        config.setHttpMethod(rs.getString("http_method"));
        config.setHeaders(rs.getString("headers"));
        config.setPayloadTemplate(rs.getString("payload_template"));
        config.setDbDatasource(rs.getString("db_datasource"));
        config.setSqlStatement(rs.getString("sql_statement"));
        config.setSqlType(rs.getString("sql_type"));
        config.setLogEnabled(rs.getBoolean("log_enabled"));
        config.setActive(rs.getBoolean("active"));
        config.setVersion(rs.getInt("version"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) config.setCreatedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) config.setUpdatedAt(updatedAt.toLocalDateTime());
        return config;
    };
}
