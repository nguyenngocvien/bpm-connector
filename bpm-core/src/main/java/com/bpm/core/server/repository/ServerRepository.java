package com.bpm.core.server.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bpm.core.server.domain.Server;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class ServerRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Server> rowMapper = new BeanPropertyRowMapper<>(Server.class);

    public ServerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Server> findAll() {
        String sql = "SELECT * FROM core_servers ORDER BY id ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Server> findById(Long id) {
        String sql = "SELECT * FROM core_servers WHERE id = ?";
        List<Server> servers = jdbcTemplate.query(sql, rowMapper, id);
        return servers.stream().findFirst();
    }

    public void insert(Server server) {
        String sql = "INSERT INTO core_servers (name, type, ip, port, https) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, server.getName());
            ps.setObject(2, server.getType().name(), java.sql.Types.VARCHAR); // Enum -> VARCHAR
            ps.setString(3, server.getIp());
            ps.setInt(4, server.getPort());
            ps.setBoolean(5, server.isHttps());
            return ps;
        });
    }

    public void update(Server server) {
        String sql = "UPDATE core_servers SET name = ?, type = ?, ip = ?, port = ?, https = ? WHERE id = ?";
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, server.getName());
            ps.setObject(2, server.getType().name(), java.sql.Types.VARCHAR); // Enum -> VARCHAR
            ps.setString(3, server.getIp());
            ps.setInt(4, server.getPort());
            ps.setBoolean(5, server.isHttps());
            ps.setLong(6, server.getId());
            return ps;
        });
    }

    public void save(Server server) {
        if (server.getId() == null) {
            insert(server);
        } else {
            update(server);
        }
    }


    public void delete(Long id) {
        String sql = "DELETE FROM core_servers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
