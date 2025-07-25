package com.bpm.core.repository;

import com.bpm.core.entity.ServiceLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Repository
public class ServiceLogRepository {

	@Autowired
    private final JdbcTemplate jdbcTemplate;

    public ServiceLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insertLog(ServiceLog log) {
        String sql = "INSERT INTO isrv_log " +
                "(service_code, request_data, mapped_request, response_data, status_code, duration_ms, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int affectedRows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, log.getServiceCode());
            ps.setString(2, log.getRequestData());
            ps.setString(3, log.getMappedRequest());
            ps.setString(4, log.getResponseData());
            ps.setInt(5, log.getStatusCode());
            ps.setInt(6, log.getDurationMs());
            ps.setTimestamp(7, Timestamp.valueOf(log.getCreatedAt()));
            return ps;
        }, keyHolder);

        if (affectedRows == 0) {
            throw new RuntimeException("Inserting log failed, no rows affected.");
        }

        Number generatedId = keyHolder.getKey();
        return (generatedId != null) ? generatedId.longValue() : null;
    }
}
