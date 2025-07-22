package com.bpm.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bpm.core.entity.ServiceConfig;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
public class ServiceConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceConfigRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<ServiceConfig> loadServiceByCode(String serviceCode) {
        String sql = "SELECT * FROM int_service_config WHERE service_code = ? AND active = true";

        try {
            ServiceConfig config = jdbcTemplate.query(
                (Connection con) -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, serviceCode);
                    return ps;
                },
                (ResultSet rs) -> {
                    if (rs.next()) {
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
                    }
                    return null;
                }
            );
            return Optional.ofNullable(config);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

