package com.bpm.core.server.service;

import com.bpm.core.server.domain.Server;
import com.bpm.core.server.repository.ServerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {

    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public Server getServerById(Long id) {
        Server server = serverRepository.findById(id)
        		.orElseThrow(() -> new IllegalArgumentException("Invalid ID")); 
    	return server;
    }

    public void saveServer(Server server) {
        serverRepository.save(server);
    }

    public void deleteServer(Long id) {
        serverRepository.delete(id);
    }
}
