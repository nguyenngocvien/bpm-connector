package com.bpm.core.repository;

import com.bpm.core.model.service.ServiceConfig;
import com.bpm.core.model.service.ServiceType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
            String sql = "INSERT INTO core_service_config "
                       + "(service_code, service_name, service_description, service_type, log_enabled, active, version, created_at, updated_at) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, config.getServiceCode());
                ps.setString(2, config.getServiceName());
                ps.setString(3, config.getServiceDescription());
                ps.setString(4, config.getServiceType().name());
                ps.setBoolean(5, Boolean.TRUE.equals(config.getLogEnabled()));
                ps.setBoolean(6, Boolean.TRUE.equals(config.getActive()));
                ps.setInt(7, config.getVersion());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                config.setId(keyHolder.getKey().longValue());
                return 1; // 1 row inserted
            }

        } else {
            // UPDATE
            String sql = "UPDATE core_service_config SET "
                       + "service_code = ?, service_name = ?, service_description = ?, service_type = ?, "
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
        return 0;
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
