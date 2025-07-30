package com.bpm.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bpm.core.entity.DbConfig;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Service
public class DbConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DbConfig> findAll() {
        String sql = "SELECT id, name, url, username, password, driver_class_name FROM core_db_config ORDER BY id ASC";
        return jdbcTemplate.query(sql, dbConfigRowMapper);
    }

    public Optional<DbConfig> findById(Long id) {
        String sql = "SELECT id, name, url, username, password, driver_class_name FROM core_db_config WHERE id = ?";
        try {
            DbConfig config = jdbcTemplate.queryForObject(sql, dbConfigRowMapper, id);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public DbConfig findByName(String name) {
        String sql = "SELECT id, name, url, username, password, driver_class_name FROM core_db_config WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, dbConfigRowMapper, name);
    }

    public int save(DbConfig config) {
        if (config.getId() == null) {
            // INSERT
            String sql = "INSERT INTO core_db_config (name, url, username, password, driver_class_name) VALUES (?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql,
                    config.getName(),
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword(),
                    config.getDriverClassName());
        } else {
            // UPDATE
            String sql = "UPDATE core_db_config SET name = ?, url = ?, username = ?, password = ?, driver_class_name = ? WHERE id = ?";
            return jdbcTemplate.update(sql,
                    config.getName(),
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword(),
                    config.getDriverClassName(),
                    config.getId());
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM core_db_config WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private final RowMapper<DbConfig> dbConfigRowMapper = (rs, rowNum) -> {
        DbConfig config = new DbConfig();
        config.setId(rs.getLong("id"));  // ensure your DbConfig has id field
        config.setName(rs.getString("name"));
        config.setUrl(rs.getString("url"));
        config.setUsername(rs.getString("username"));
        config.setPassword(rs.getString("password"));
        config.setDriverClassName(rs.getString("driver_class_name"));
        return config;
    };
}
