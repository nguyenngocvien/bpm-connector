package com.bpm.core.repository;

import com.bpm.core.entity.ServiceConfig;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class ServiceConfigRepository {

    private final DataSource dataSource;

    public ServiceConfigRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<ServiceConfig> loadServiceByCode(String serviceCode) {
        String sql = "SELECT * FROM isrv_config WHERE service_code = ? AND active = true";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, serviceCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ServiceConfig sc = new ServiceConfig();
                sc.setId(rs.getLong("id"));
                sc.setServiceCode(rs.getString("service_code"));
                sc.setTargetUrl(rs.getString("target_url"));
                sc.setHttpMethod(rs.getString("http_method"));
                sc.setHeaders(rs.getString("headers"));
                sc.setPayloadMapping(rs.getString("payload_mapping"));
                sc.setActive(rs.getBoolean("active"));
                sc.setLogEnabled(rs.getBoolean("log_enabled"));
                sc.setVersion(rs.getInt("version"));
                sc.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                sc.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

                return Optional.of(sc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}

