package com.bpm.core.repository;

import com.bpm.core.entity.ServiceConfig;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ServiceConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ServiceConfig> loadServiceByCode(String serviceCode) {
        String sql = "SELECT * FROM isrv_config WHERE service_code = ? AND active = true";

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
