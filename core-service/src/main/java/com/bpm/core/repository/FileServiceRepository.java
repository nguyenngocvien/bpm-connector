package com.bpm.core.repository;

import com.bpm.core.model.file.FileServiceConfig;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
                    Timestamp.valueOf(LocalDateTime.now()),
                    config.getConfigId()
            );
        } else {
            // INSERT
            String sql = ""
                    + "INSERT INTO core_service_file ("
                    + "config_id, file_path, file_action, file_format, file_template,"
                    + "file_name_pattern, file_encoding, extra_config,"
                    + "retry_count, retry_backoff_ms, timeout_ms,"
                    + "active, created_at, updated_at"
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
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
                    Timestamp.valueOf(now),
                    Timestamp.valueOf(now)
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

    // --- RowMapper
    private final RowMapper<FileServiceConfig> rowMapper = (rs, rowNum) -> FileServiceConfig.builder()
            .configId(rs.getLong("config_id"))
            .filePath(rs.getString("file_path"))
            .fileAction(rs.getString("file_action"))
            .fileFormat(rs.getString("file_format"))
            .fileTemplate(rs.getString("file_template"))
            .fileNamePattern(rs.getString("file_name_pattern"))
            .fileEncoding(rs.getString("file_encoding"))
            .extraConfig(rs.getString("extra_config"))
            .retryCount(rs.getInt("retry_count"))
            .retryBackoffMs(rs.getInt("retry_backoff_ms"))
            .timeoutMs(rs.getInt("timeout_ms"))
            .active(rs.getBoolean("active"))
            .createdAt(toLocalDateTime(rs.getTimestamp("created_at")))
            .updatedAt(toLocalDateTime(rs.getTimestamp("updated_at")))
            .build();

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
