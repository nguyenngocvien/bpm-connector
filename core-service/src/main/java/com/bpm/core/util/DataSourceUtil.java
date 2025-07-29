package com.bpm.core.util;

import com.bpm.core.config.DbConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class DataSourceUtil {

    public static DataSource createDataSource(DbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());

        // Optional: Pool config
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setConnectionTimeout(30000); // 30s
        hikariConfig.setIdleTimeout(600000); // 10 minutes
        hikariConfig.setMaxLifetime(1800000); // 30 minutes

        // Optional: Name this pool for monitoring
        hikariConfig.setPoolName("ds-" + config.getName());

        return new HikariDataSource(hikariConfig);
    }

    public static JdbcTemplate createJdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
