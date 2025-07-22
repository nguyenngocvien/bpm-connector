package com.bpm.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bpm.core.entity.ServiceLog;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Repository
public class ServiceLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insertLog(ServiceLog log) {
        String sql = "INSERT INTO int_log " +
                     "(service_code, request_data, mapped_request, response_data, status_code, duration_ms, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, log.getServiceCode());
            ps.setString(2, log.getRequestData());
            ps.setString(3, log.getMappedRequest());
            ps.setString(4, log.getResponseData());
            ps.setInt(5, log.getStatusCode());
            ps.setInt(6, log.getDurationMs());
            ps.setTimestamp(7, Timestamp.valueOf(log.getCreatedAt()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }
}

