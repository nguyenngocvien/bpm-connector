package com.bpm.core.mail.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bpm.core.mail.domain.MailServiceConfig;

import java.util.List;
import java.util.Optional;

public class MailServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public MailServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- Find all mail configs
    public List<MailServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_mail ORDER BY config_id ASC";
        return jdbcTemplate.query(sql, mailRowMapper);
    }

    // --- Find by config_id
    public Optional<MailServiceConfig> findById(Long configId) {
        String sql = "SELECT * FROM core_service_mail WHERE config_id = ?";
        try {
            MailServiceConfig config = jdbcTemplate.queryForObject(sql, mailRowMapper, configId);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // --- Save (Insert or Update)
    public int save(MailServiceConfig config) {
        if (config.getConfigId() == null) {
            throw new IllegalArgumentException("config_id cannot be null (FK to core_service_config)");
        }

        // Check if exists
        Optional<MailServiceConfig> existing = findById(config.getConfigId());
        if (existing.isPresent()) {
            // UPDATE
            String sql = ""
                    + "UPDATE core_service_mail SET"
                    + "smtp_server = ?, smtp_port = ?, username = ?, password = ?, use_tls = ?,"
                    + "mail_from = ?, mail_to = ?, mail_cc = ?, mail_bcc = ?,"
                    + "subject_template = ?, body_template = ?, headers = ?,"
                    + "retry_count = ?, retry_backoff_ms = ?, timeout_ms = ?,"
                    + "attachments_enabled = ?, active = ?, updated_at = CURRENT_TIMESTAMP"
                    + "WHERE config_id = ?";
            
            return jdbcTemplate.update(sql,
                    config.getSmtpServer(),
                    config.getSmtpPort(),
                    config.getUsername(),
                    config.getPassword(),
                    config.getUseTls(),
                    config.getMailFrom(),
                    config.getMailTo(),
                    config.getMailCc(),
                    config.getMailBcc(),
                    config.getSubjectTemplate(),
                    config.getBodyTemplate(),
                    config.getHeaders(),
                    config.getRetryCount(),
                    config.getRetryBackoffMs(),
                    config.getTimeoutMs(),
                    config.getAttachmentsEnabled(),
                    config.getActive(),
                    config.getConfigId()
            );
        } else {
            // INSERT
            String sql = ""
            		+ "INSERT INTO core_service_mail ("
            		+ "config_id, smtp_server, smtp_port, username, password, use_tls,"
                    + "mail_from, mail_to, mail_cc, mail_bcc,"
                    + " subject_template, body_template, headers,"
                    + "retry_count, retry_backoff_ms, timeout_ms,"
                    + "attachments_enabled, active, created_at, updated_at"
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
            
            return jdbcTemplate.update(sql,
                    config.getConfigId(),
                    config.getSmtpServer(),
                    config.getSmtpPort(),
                    config.getUsername(),
                    config.getPassword(),
                    config.getUseTls(),
                    config.getMailFrom(),
                    config.getMailTo(),
                    config.getMailCc(),
                    config.getMailBcc(),
                    config.getSubjectTemplate(),
                    config.getBodyTemplate(),
                    config.getHeaders(),
                    config.getRetryCount(),
                    config.getRetryBackoffMs(),
                    config.getTimeoutMs(),
                    config.getAttachmentsEnabled(),
                    config.getActive()
            );
        }
    }

    // --- Delete
    public int deleteById(Long configId) {
        String sql = "DELETE FROM core_service_mail WHERE config_id = ?";
        return jdbcTemplate.update(sql, configId);
    }

    // --- RowMapper
    private final RowMapper<MailServiceConfig> mailRowMapper = (rs, rowNum) -> {
        return MailServiceConfig.builder()
                .configId(rs.getLong("config_id"))
                .smtpServer(rs.getString("smtp_server"))
                .smtpPort(rs.getInt("smtp_port"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .useTls(rs.getBoolean("use_tls"))
                .mailFrom(rs.getString("mail_from"))
                .mailTo(rs.getString("mail_to"))
                .mailCc(rs.getString("mail_cc"))
                .mailBcc(rs.getString("mail_bcc"))
                .subjectTemplate(rs.getString("subject_template"))
                .bodyTemplate(rs.getString("body_template"))
                .headers(rs.getString("headers"))
                .retryCount(rs.getInt("retry_count"))
                .retryBackoffMs(rs.getInt("retry_backoff_ms"))
                .timeoutMs(rs.getInt("timeout_ms"))
                .attachmentsEnabled(rs.getBoolean("attachments_enabled"))
                .active(rs.getBoolean("active"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    };
}