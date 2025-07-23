package com.bpm.core.repository;

import com.bpm.core.entity.ServiceLog;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import java.sql.*;
import javax.sql.DataSource;

public class ServiceLogRepository {

    private final DataSource dataSource;

    public ServiceLogRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long insertLog(ServiceLog log) {
        String sql = "INSERT INTO isrv_log " +
                     "(service_code, request_data, mapped_request, response_data, status_code, duration_ms, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, log.getServiceCode());
            ps.setString(2, log.getRequestData());
            ps.setString(3, log.getMappedRequest());
            ps.setString(4, log.getResponseData());
            ps.setInt(5, log.getStatusCode());
            ps.setInt(6, log.getDurationMs());
            ps.setTimestamp(7, Timestamp.valueOf(log.getCreatedAt()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting log failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Inserting log failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

