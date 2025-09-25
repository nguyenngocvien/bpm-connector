package com.bpm.core.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.server.domain.Server;

public interface ServerConfigRepository extends JpaRepository<Server, Long> {

	Server findByName(String name);

	Server findByIpAndPort(String ip, Integer port);
	
	List<Server> findByType(String type);
}
