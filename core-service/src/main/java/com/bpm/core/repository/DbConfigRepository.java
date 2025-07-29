package com.bpm.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bpm.core.config.DbConfig;

@Service
public class DbConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DbConfig findByName(String name) {
        String sql = "SELECT name, url, username, password, driver_class_name FROM core_db_config WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            DbConfig config = new DbConfig();
            config.setName(rs.getString("name"));
            config.setUrl(rs.getString("url"));
            config.setUsername(rs.getString("username"));
            config.setPassword(rs.getString("password"));
            config.setDriverClassName(rs.getString("driver_class_name"));
            return config;
        }, name);
    }
}
