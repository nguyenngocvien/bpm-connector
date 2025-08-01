package com.bpm.core.util;

import com.bpm.core.model.db.DataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

public class DataSourceTestUtil {

    public static boolean testConnection(DataSourceConfig config) throws Exception {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());

        // Optional: setup timeout và test query
        hikariConfig.setConnectionTimeout(3000);  // 3s timeout
        hikariConfig.setValidationTimeout(2000);
        hikariConfig.setMaximumPoolSize(1);
        hikariConfig.setMinimumIdle(0);
        hikariConfig.setIdleTimeout(10000);
        hikariConfig.setMaxLifetime(30000);
        hikariConfig.setInitializationFailTimeout(-1);  // không fail khi khởi tạo
        
        if (config.getMaxPoolSize() != null) {
            hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
        }
        if (config.getIdleTimeoutMs() != null) {
            hikariConfig.setIdleTimeout(config.getIdleTimeoutMs());
        }

        // Test query (base on DBMS, as H2/MySQL/PostgreSQL)
        hikariConfig.setConnectionTestQuery("SELECT 1");

        try (HikariDataSource ds = new HikariDataSource(hikariConfig);
             Connection conn = ds.getConnection()) {
            return conn != null && !conn.isClosed();
        }
    }
}
