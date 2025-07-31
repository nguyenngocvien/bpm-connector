package com.bpm.core.repository;

import com.bpm.core.model.service.ServiceConfig;
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

    public List<ServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_config ORDER BY id ASC";
        return jdbcTemplate.query(sql, serviceConfigRowMapper);
    }

    public Optional<ServiceConfig> findById(Long id) {
        String sql = "SELECT * FROM core_service_config WHERE id = ?";
        try {
            ServiceConfig config = jdbcTemplate.queryForObject(sql, serviceConfigRowMapper, id);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<ServiceConfig> findByCode(String serviceCode) {
        String sql = "SELECT * FROM core_service_config WHERE service_code = ?";
        try {
            ServiceConfig config = jdbcTemplate.queryForObject(sql, serviceConfigRowMapper, serviceCode);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(ServiceConfig config) {
        if (config.getId() == null) {
            // INSERT
            String sql = ""
                + "INSERT INTO core_service_config "
                + "(service_code, service_name, service_description, service_type, log_enabled, active, version, created_at, updated_at)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
            return jdbcTemplate.update(sql,
                    config.getServiceCode(),
                    config.getServiceName(),
                    config.getServiceDescription(),
                    config.getServiceType(),
                    config.getLogEnabled(),
                    config.getActive(),
                    config.getVersion());
        } else {
            // UPDATE
            String sql = "UPDATE core_service_config SET service_code = ?, service_name = ?, service_description = ?, service_type = ?, log_enabled = ?, active = ?, version = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            return jdbcTemplate.update(sql,
                    config.getServiceCode(),
                    config.getServiceName(),
                    config.getServiceDescription(),
                    config.getServiceType(),
                    config.getLogEnabled(),
                    config.getActive(),
                    config.getVersion(),
                    config.getId());
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM core_service_config WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private final RowMapper<ServiceConfig> serviceConfigRowMapper = (rs, rowNum) -> ServiceConfig.builder()
            .id(rs.getLong("id"))
            .serviceCode(rs.getString("service_code"))
            .serviceName(rs.getString("service_name"))
            .serviceDescription(rs.getString("service_description"))
            .serviceType(rs.getString("service_type"))
            .logEnabled(rs.getBoolean("log_enabled"))
            .active(rs.getBoolean("active"))
            .version(rs.getInt("version"))
            .createdAt(toLocalDateTime(rs.getTimestamp("created_at")))
            .updatedAt(toLocalDateTime(rs.getTimestamp("updated_at")))
            .build();

    private static java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
