package com.bpm.core.repository;

import com.bpm.core.model.rest.RestHeader;
import com.bpm.core.model.rest.RestPathParam;
import com.bpm.core.model.rest.RestQueryParam;
import com.bpm.core.model.rest.RestServiceConfig;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RestServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public RestServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RestServiceConfig> findAll() {
        String sql = "SELECT * FROM core_service_rest";
        return jdbcTemplate.query(sql, new RestServiceRowMapper());
    }

    public Optional<RestServiceConfig> findById(Long id) {
        String sql = "SELECT * FROM core_service_rest WHERE id = ?";
        try {
            RestServiceConfig config = jdbcTemplate.queryForObject(sql, new RestServiceRowMapper(), id);
            if (config != null) {
                loadDetails(config);
            }
            return Optional.ofNullable(config);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(RestServiceConfig config) {
        if (config.getId() == null) {
            String sql = "INSERT INTO core_service_rest (target_url, http_method, content_type, timeout_ms, retry_count, retry_backoff_ms, payload_template, response_mapping, auth_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
            Long newId = jdbcTemplate.queryForObject(sql, Long.class,
                    config.getTargetUrl(),
                    config.getHttpMethod(),
                    config.getContentType(),
                    config.getTimeoutMs(),
                    config.getRetryCount(),
                    config.getRetryBackoffMs(),
                    config.getPayloadTemplate(),
                    config.getResponseMapping(),
                    config.getAuthId());
            config.setId(newId);
        } else {
            String sql = "UPDATE core_service_rest SET target_url = ?, http_method = ?, content_type = ?, timeout_ms = ?, retry_count = ?, retry_backoff_ms = ?, payload_template = ?, response_mapping = ?, auth_id = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    config.getTargetUrl(),
                    config.getHttpMethod(),
                    config.getContentType(),
                    config.getTimeoutMs(),
                    config.getRetryCount(),
                    config.getRetryBackoffMs(),
                    config.getPayloadTemplate(),
                    config.getResponseMapping(),
                    config.getAuthId(),
                    config.getId());
        }

        saveHeaders(config);
        saveQueryParams(config);
        savePathParams(config);

        return 1;
    }

    public int deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM core_service_rest_header WHERE rest_config_id = ?", id);
        jdbcTemplate.update("DELETE FROM core_service_rest_query_param WHERE rest_config_id = ?", id);
        jdbcTemplate.update("DELETE FROM core_service_rest_path_param WHERE rest_config_id = ?", id);
        return jdbcTemplate.update("DELETE FROM core_service_rest WHERE id = ?", id);
    }

    private void saveHeaders(RestServiceConfig config) {
        jdbcTemplate.update("DELETE FROM core_service_rest_header WHERE rest_config_id = ?", config.getId());
        if (config.getHeaders() != null) {
            for (RestHeader header : config.getHeaders()) {
                jdbcTemplate.update("INSERT INTO core_service_rest_header (rest_config_id, header_name, header_value) VALUES (?, ?, ?)",
                        config.getId(), header.getHeaderName(), header.getHeaderValue());
            }
        }
    }

    private void saveQueryParams(RestServiceConfig config) {
        jdbcTemplate.update("DELETE FROM core_service_rest_query_param WHERE rest_config_id = ?", config.getId());
        if (config.getQueryParams() != null) {
            for (RestQueryParam param : config.getQueryParams()) {
                jdbcTemplate.update("INSERT INTO core_service_rest_query_param (rest_config_id, param_name, param_value) VALUES (?, ?, ?)",
                        config.getId(), param.getParamName(), param.getParamValue());
            }
        }
    }

    private void savePathParams(RestServiceConfig config) {
        jdbcTemplate.update("DELETE FROM core_service_rest_path_param WHERE rest_config_id = ?", config.getId());
        if (config.getPathParams() != null) {
            for (RestPathParam param : config.getPathParams()) {
                jdbcTemplate.update("INSERT INTO core_service_rest_path_param (rest_config_id, param_name, param_value) VALUES (?, ?, ?)",
                        config.getId(), param.getParamName(), param.getParamValue());
            }
        }
    }

    private void loadDetails(RestServiceConfig config) {
        List<RestHeader> headers = jdbcTemplate.query(
                "SELECT * FROM core_service_rest_header WHERE rest_config_id = ?",
                (rs, rowNum) -> new RestHeader(rs.getLong("id"), rs.getString("header_name"), rs.getString("header_value")),
                config.getId());
        config.setHeaders(headers);

        List<RestQueryParam> queryParams = jdbcTemplate.query(
                "SELECT * FROM core_service_rest_query_param WHERE rest_config_id = ?",
                (rs, rowNum) -> new RestQueryParam(rs.getLong("id"), rs.getString("param_name"), rs.getString("param_value")),
                config.getId());
        config.setQueryParams(queryParams);

        List<RestPathParam> pathParams = jdbcTemplate.query(
                "SELECT * FROM core_service_rest_path_param WHERE rest_config_id = ?",
                (rs, rowNum) -> new RestPathParam(rs.getLong("id"), rs.getString("param_name"), rs.getString("param_value")),
                config.getId());
        config.setPathParams(pathParams);
    }

    private static class RestServiceRowMapper implements RowMapper<RestServiceConfig> {
        @Override
        public RestServiceConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
            return RestServiceConfig.builder()
                    .id(rs.getLong("id"))
                    .targetUrl(rs.getString("target_url"))
                    .httpMethod(rs.getString("http_method"))
                    .contentType(rs.getString("content_type"))
                    .timeoutMs(rs.getInt("timeout_ms"))
                    .retryCount(rs.getInt("retry_count"))
                    .retryBackoffMs(rs.getInt("retry_backoff_ms"))
                    .payloadTemplate(rs.getString("payload_template"))
                    .responseMapping(rs.getString("response_mapping"))
                    .authId(rs.getObject("auth_id") != null ? rs.getInt("auth_id") : null)
                    .build();
        }
    }
}
