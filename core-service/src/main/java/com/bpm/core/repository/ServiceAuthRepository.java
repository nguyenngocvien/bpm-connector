package com.bpm.core.repository;

import java.sql.PreparedStatement;
import java.util.Optional;

import javax.sql.DataSource;
import com.bpm.core.entity.User;

import java.sql.*;

public class ServiceAuthRepository {

    private final DataSource dataSource;

    public ServiceAuthRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM isrv_auth WHERE username = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}

