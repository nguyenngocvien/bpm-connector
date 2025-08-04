package com.bpm.core.util;

import com.bpm.core.model.db.DataSourceConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceTestUtil {

    public static boolean testConnection(DataSourceConfig config) throws Exception {
        // Load driver
        Class.forName(config.getDriverClassName());

        // Setup timeout JDBC
        DriverManager.setLoginTimeout(3); // 3s timeout

        // open connection
        try (Connection conn = DriverManager.getConnection(
                config.getJdbcUrl(),
                config.getUsername(),
                config.getPassword())) {

            if (conn == null || conn.isClosed()) return false;

            // Test query
            String testQuery = getTestQuery(config.getDriverClassName());
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(testQuery);
            }

            return true;
        } catch (SQLException e) {
            throw new Exception("Connection test failed: " + e.getMessage(), e);
        }
    }

    private static String getTestQuery(String driverClass) {
        if (driverClass.contains("oracle")) {
            return "SELECT 1 FROM DUAL";
        } else if (driverClass.contains("sqlserver")) {
            return "SELECT 1";
        } else if (driverClass.contains("postgresql")) {
            return "SELECT 1";
        } else if (driverClass.contains("mysql")) {
            return "SELECT 1";
        } else if (driverClass.contains("h2")) {
            return "SELECT 1";
        }
        return "SELECT 1";  // fallback query
    }
}
