package com.bpm.core.repository;

import com.bpm.core.entity.ServiceConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServiceConfigRepository {

	@Autowired
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
            ServiceConfig sc = jdbcTemplate.queryForObject(sql, serviceConfigRowMapper, id);
            return Optional.ofNullable(sc);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<ServiceConfig> loadServiceByCode(String serviceCode) {
        String sql = "SELECT * FROM core_service_config WHERE service_code = ? AND active = true";

        try {
            ServiceConfig sc = jdbcTemplate.queryForObject(sql, serviceConfigRowMapper, serviceCode);
            return Optional.ofNullable(sc);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    public int insert(ServiceConfig config) {
        String sql = "INSERT INTO core_service_config (service_code, target_url, http_method, headers, payload_mapping, active, log_enabled, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                config.getServiceCode(),
                config.getTargetUrl(),
                config.getHttpMethod(),
                config.getHeaders(),
                config.getPayloadMapping(),
                config.getActive(),
                config.getLogEnabled(),
                config.getVersion());
    }
    
    public int deleteById(Long id) {
        String sql = "DELETE FROM core_service_config WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    public int update(ServiceConfig config) {
        String sql = "UPDATE core_service_config SET service_code = ?, target_url = ?, http_method = ?, headers = ?, payload_mapping = ?, active = ?, log_enabled = ?, version = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        return jdbcTemplate.update(sql,
                config.getServiceCode(),
                config.getTargetUrl(),
                config.getHttpMethod(),
                config.getHeaders(),
                config.getPayloadMapping(),
                config.getActive(),
                config.getLogEnabled(),
                config.getVersion(),
                config.getId());
    }

    private final RowMapper<ServiceConfig> serviceConfigRowMapper = (rs, rowNum) -> {
        ServiceConfig sc = new ServiceConfig();
        sc.setId(rs.getLong("id"));
        sc.setServiceCode(rs.getString("service_code"));
        sc.setTargetUrl(rs.getString("target_url"));
        sc.setHttpMethod(rs.getString("http_method"));
        sc.setHeaders(rs.getString("headers"));
        sc.setPayloadMapping(rs.getString("payload_mapping"));
        sc.setActive(rs.getBoolean("active"));
        sc.setLogEnabled(rs.getBoolean("log_enabled"));
        sc.setVersion(rs.getInt("version"));
        sc.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        sc.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return sc;
    };
}
