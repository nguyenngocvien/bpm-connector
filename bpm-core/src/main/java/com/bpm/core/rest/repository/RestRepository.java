package com.bpm.core.rest.repository;

import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.infrastructure.RestServiceConfigParser;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;
import java.util.List;

public class RestRepository {

    private final JdbcTemplate jdbcTemplate;

    public RestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RestServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_rest";
        return jdbcTemplate.query(sql, rowMapperWithJsonParsing);
    }

    public Optional<RestServiceConfig> findById(Long id) {
        String sql = "SELECT * FROM core_service_rest WHERE id = ?";
        try {
            RestServiceConfig config = jdbcTemplate.queryForObject(sql, rowMapperWithJsonParsing, id);
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(RestServiceConfig config, Long serviceId) {
        if (config == null) return;
        config.setId(serviceId);

        // Serialize JSON fields before saving
        config.setHeaders(RestServiceConfigParser.toJson(config.getHeaderList()));
        config.setQueryParams(RestServiceConfigParser.toJson(config.getQueryParamList()));
        config.setPathParams(RestServiceConfigParser.toJson(config.getPathParamList()));

        String sql = ""
                + "INSERT INTO core_service_rest (id, server_id, path, http_method, content_type, timeout_ms, retry_count, retry_backoff_ms, "
                + "payload_template, response_mapping, auth_id, headers, query_params, path_params, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) "
                + "ON CONFLICT (id) DO UPDATE SET "
                + "server_id = EXCLUDED.server_id, path = EXCLUDED.path, http_method = EXCLUDED.http_method, content_type = EXCLUDED.content_type, "
                + "timeout_ms = EXCLUDED.timeout_ms, retry_count = EXCLUDED.retry_count, retry_backoff_ms = EXCLUDED.retry_backoff_ms, "
                + "payload_template = EXCLUDED.payload_template, response_mapping = EXCLUDED.response_mapping, auth_id = EXCLUDED.auth_id, "
                + "headers = EXCLUDED.headers, query_params = EXCLUDED.query_params, path_params = EXCLUDED.path_params, "
                + "updated_at = CURRENT_TIMESTAMP";

        jdbcTemplate.update(sql,
                config.getId(),
                config.getServerId(),
                config.getPath(),
                config.getHttpMethod(),
                config.getContentType(),
                config.getTimeoutMs(),
                config.getRetryCount(),
                config.getRetryBackoffMs(),
                config.getPayloadTemplate(),
                config.getResponseMapping(),
                config.getAuthId(),
                config.getHeaders(),
                config.getQueryParams(),
                config.getPathParams()
        );
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM core_service_rest WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private final RowMapper<RestServiceConfig> rowMapperWithJsonParsing = (rs, rowNum) -> {
        RestServiceConfig config = new RestServiceConfig();
        config.setId(rs.getLong("id"));
        Object serverIdObj = rs.getObject("server_id");
        config.setServerId(serverIdObj != null ? rs.getLong("server_id") : null);
        config.setPath(rs.getString("path"));
        config.setHttpMethod(rs.getString("http_method"));
        config.setContentType(rs.getString("content_type"));
        config.setTimeoutMs(rs.getInt("timeout_ms"));
        config.setRetryCount(rs.getInt("retry_count"));
        config.setRetryBackoffMs(rs.getInt("retry_backoff_ms"));
        config.setPayloadTemplate(rs.getString("payload_template"));
        config.setResponseMapping(rs.getString("response_mapping"));
        Object authIdObj = rs.getObject("auth_id");
        config.setAuthId(authIdObj != null ? rs.getInt("auth_id") : null);
        config.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        config.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Parse JSON to List<NameValuePair>
        config.setHeaders(rs.getString("headers"));
        config.setQueryParams(rs.getString("query_params"));
        config.setPathParams(rs.getString("path_params"));

        config.setHeaderList(RestServiceConfigParser.parseHeaders(config.getHeaders()));
        config.setQueryParamList(RestServiceConfigParser.parseQueryParams(config.getQueryParams()));
        config.setPathParamList(RestServiceConfigParser.parsePathParams(config.getPathParams()));

        return config;
    };
}
