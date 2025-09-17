package com.bpm.core.serviceconfig.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class ServiceConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_config ORDER BY id ASC";
        return jdbcTemplate.query(sql, serviceConfigRowMapper);
    }

    
    public List<ServiceConfig> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM core_service_config WHERE service_code ILIKE ? OR service_name ILIKE ?";
        String like = "%" + keyword + "%";
        return jdbcTemplate.query(sql, serviceConfigRowMapper, like, like);
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
    
    public int enableLog(Long id, boolean enabled) {
        String sql = "UPDATE core_service_config SET log_enabled = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        return jdbcTemplate.update(sql, enabled, id);
    }

    public int setActive(Long id, boolean active) {
        String sql = "UPDATE core_service_config SET active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        return jdbcTemplate.update(sql, active, id);
    }

    public int save(ServiceConfig config) {
        if (config.getId() == null) {
            // INSERT with RETURNING id
            String sql = "INSERT INTO core_service_config "
                       + "(service_code, service_name, service_description, service_type, log_enabled, active, version, created_at, updated_at) "
                       + "VALUES (?, ?, ?, ?::service_type_enum, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) "
                       + "RETURNING id";

            Long newId = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> rs.getLong("id"),  // RowMapper for id only
                    config.getServiceCode(),
                    config.getServiceName(),
                    config.getServiceDescription(),
                    config.getServiceType().name(),
                    Boolean.TRUE.equals(config.getLogEnabled()),
                    Boolean.TRUE.equals(config.getActive()),
                    config.getVersion()
            );

            config.setId(newId);
            return 1; // 1 row inserted
        } else {
            // UPDATE
            String sql = "UPDATE core_service_config SET "
                       + "service_code = ?, service_name = ?, service_description = ?, service_type = ?::service_type_enum, "
                       + "log_enabled = ?, active = ?, version = ?, updated_at = CURRENT_TIMESTAMP "
                       + "WHERE id = ?";

            return jdbcTemplate.update(sql,
                    config.getServiceCode(),
                    config.getServiceName(),
                    config.getServiceDescription(),
                    config.getServiceType().name(),
                    Boolean.TRUE.equals(config.getLogEnabled()),
                    Boolean.TRUE.equals(config.getActive()),
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
            .serviceType(ServiceType.valueOf(rs.getString("service_type")))
            .logEnabled(getBooleanOrNull(rs.getObject("log_enabled")))
            .active(getBooleanOrNull(rs.getObject("active")))
            .version(rs.getInt("version"))
            .createdAt(toLocalDateTime(rs.getTimestamp("created_at")))
            .updatedAt(toLocalDateTime(rs.getTimestamp("updated_at")))
            .build();

    private static java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private static Boolean getBooleanOrNull(Object obj) {
        return obj != null ? ((Boolean) obj) : null;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM core_service_config WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
