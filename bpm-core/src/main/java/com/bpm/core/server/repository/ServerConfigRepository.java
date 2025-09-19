package com.bpm.core.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.server.domain.Server;

public interface ServerConfigRepository extends JpaRepository<Server, Long> {

	Server findByName(String name);

	Server findByIpAndPort(String ip, Integer port);
}
