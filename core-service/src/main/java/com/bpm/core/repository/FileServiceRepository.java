package com.bpm.core.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bpm.core.model.fncmis.FileServiceConfig;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FileServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public FileServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FileServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_file ORDER BY config_id ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<FileServiceConfig> findById(Long configId) {
        String sql = "SELECT * FROM core_service_file WHERE config_id = ?";
        try {
            FileServiceConfig config = jdbcTemplate.queryForObject(sql, rowMapper, configId);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(FileServiceConfig config) {
        LocalDateTime now = LocalDateTime.now();
        config.setUpdatedAt(now);

        if (existsById(config.getConfigId())) {
            // UPDATE
            String sql = ""
                    + "UPDATE core_service_file SET"
                    + "file_path = ?, file_action = ?, file_format = ?, file_template = ?,"
                    + "file_name_pattern = ?, file_encoding = ?, extra_config = ?,"
                    + "retry_count = ?, retry_backoff_ms = ?, timeout_ms = ?,"
                    + "active = ?, updated_at = ?"
                    + "WHERE config_id = ?";
            
            return jdbcTemplate.update(sql,
                    config.getFilePath(),
                    config.getFileAction(),
                    config.getFileFormat(),
                    config.getFileTemplate(),
                    config.getFileNamePattern(),
                    config.getFileEncoding(),
                    config.getExtraConfig(),
                    config.getRetryCount(),
                    config.getRetryBackoffMs(),
                    config.getTimeoutMs(),
                    config.getActive(),
                    Timestamp.valueOf(config.getUpdatedAt()),
                    config.getConfigId()
            );
        } else {
            // INSERT
            config.setCreatedAt(now);
            String sql = ""
                    + "INSERT INTO core_service_file ("
                    + "config_id, file_path, file_action, file_format, file_template,"
                    + "file_name_pattern, file_encoding, extra_config,"
                    + "retry_count, retry_backoff_ms, timeout_ms,"
                    + "active, created_at, updated_at"
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            return jdbcTemplate.update(sql,
                    config.getConfigId(),
                    config.getFilePath(),
                    config.getFileAction(),
                    config.getFileFormat(),
                    config.getFileTemplate(),
                    config.getFileNamePattern(),
                    config.getFileEncoding(),
                    config.getExtraConfig(),
                    config.getRetryCount(),
                    config.getRetryBackoffMs(),
                    config.getTimeoutMs(),
                    config.getActive(),
                    Timestamp.valueOf(config.getCreatedAt()),
                    Timestamp.valueOf(config.getUpdatedAt())
            );
        }
    }

    public int deleteById(Long configId) {
        String sql = "DELETE FROM core_service_file WHERE config_id = ?";
        return jdbcTemplate.update(sql, configId);
    }

    public boolean existsById(Long configId) {
        String sql = "SELECT COUNT(*) FROM core_service_file WHERE config_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, configId);
        return count != null && count > 0;
    }

    // --- RowMapper (sử dụng constructor mặc định + setter)
    private final RowMapper<FileServiceConfig> rowMapper = (rs, rowNum) -> {
        FileServiceConfig config = new FileServiceConfig();
        config.setConfigId(rs.getLong("config_id"));
        config.setFilePath(rs.getString("file_path"));
        config.setFileAction(rs.getString("file_action"));
        config.setFileFormat(rs.getString("file_format"));
        config.setFileTemplate(rs.getString("file_template"));
        config.setFileNamePattern(rs.getString("file_name_pattern"));
        config.setFileEncoding(rs.getString("file_encoding"));
        config.setExtraConfig(rs.getString("extra_config"));
        config.setRetryCount(rs.getInt("retry_count"));
        config.setRetryBackoffMs(rs.getInt("retry_backoff_ms"));
        config.setTimeoutMs(rs.getInt("timeout_ms"));
        config.setActive(rs.getBoolean("active"));
        config.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
        config.setUpdatedAt(toLocalDateTime(rs.getTimestamp("updated_at")));
        return config;
    };

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
