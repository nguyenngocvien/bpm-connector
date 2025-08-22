package com.bpm.core.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bpm.core.model.db.DataSourceConfig;

import java.util.List;
import java.util.Optional;

@Repository
public class DataSourceRepository {

    private final JdbcTemplate jdbcTemplate;

    public DataSourceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DataSourceConfig> findAll() {
        String sql = "SELECT * FROM core_datasource ORDER BY id ASC";
        return jdbcTemplate.query(sql, dbConfigRowMapper);
    }

    public Optional<DataSourceConfig> findById(Long id) {
        String sql = "SELECT * FROM core_datasource WHERE id = ?";
        try {
        	DataSourceConfig config = jdbcTemplate.queryForObject(sql, dbConfigRowMapper, id);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<DataSourceConfig> findByName(String name) {
        String sql = "SELECT * FROM core_datasource WHERE name = ?";
        try {
        	DataSourceConfig config = jdbcTemplate.queryForObject(sql, dbConfigRowMapper, name);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(DataSourceConfig config) {
        if (config.getId() == null) {
            // INSERT
            String sql = "INSERT INTO core_datasource (name, description, url, username, password, driver_class_name, max_pool_size, min_idle, connection_timeout_ms, idle_timeout_ms, max_lifetime_ms, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql,
                    config.getName(),
                    config.getDescription(),
                    config.getJdbcUrl(),
                    config.getUsername(),
                    config.getPassword(),
                    config.getDriverClassName(),
                    config.getMaxPoolSize(),
                    config.getMinIdle(),
                    config.getConnectionTimeoutMs(),
                    config.getIdleTimeoutMs(),
                    config.getMaxLifetimeMs(),
                    config.getActive());
        } else {
            // UPDATE
            String sql = "UPDATE core_datasource SET name = ?, description = ?, url = ?, username = ?, password = ?, driver_class_name = ?, max_pool_size = ?, min_idle = ?, connection_timeout_ms = ?, idle_timeout_ms = ?, max_lifetime_ms = ?, active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            return jdbcTemplate.update(sql,
                    config.getName(),
                    config.getDescription(),
                    config.getJdbcUrl(),
                    config.getUsername(),
                    config.getPassword(),
                    config.getDriverClassName(),
                    config.getMaxPoolSize(),
                    config.getMinIdle(),
                    config.getConnectionTimeoutMs(),
                    config.getIdleTimeoutMs(),
                    config.getMaxLifetimeMs(),
                    config.getActive(),
                    config.getId());
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM core_datasource WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private final RowMapper<DataSourceConfig> dbConfigRowMapper = (rs, rowNum) -> {
        return DataSourceConfig.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .jdbcUrl(rs.getString("url"))
            .username(rs.getString("username"))
            .password(rs.getString("password"))
            .driverClassName(rs.getString("driver_class_name"))
            .maxPoolSize(rs.getInt("max_pool_size"))
            .minIdle(rs.getInt("min_idle"))
            .connectionTimeoutMs(rs.getInt("connection_timeout_ms"))
            .idleTimeoutMs(rs.getInt("idle_timeout_ms"))
            .maxLifetimeMs(rs.getInt("max_lifetime_ms"))
            .active(rs.getBoolean("active"))
            .createdAt(rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime()
                : null)
            .updatedAt(rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime()
                : null)
            .build();
    };
}
