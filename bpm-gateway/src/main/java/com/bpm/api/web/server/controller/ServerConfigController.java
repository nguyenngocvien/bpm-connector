package com.bpm.api.web.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bpm.api.constant.ROUTES;
import com.bpm.core.cmis.service.CmisSessionConfigService;
import com.bpm.core.server.domain.ServerConfig;
import com.bpm.core.server.domain.ServerType;
import com.bpm.core.server.service.ServerRepositoryService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ROUTES.UI_SERVER)
public class ServerConfigController {

    private final ServerRepositoryService serverService;
    private final CmisSessionConfigService cmisSessionService;

    public ServerConfigController(ServerRepositoryService serverService, CmisSessionConfigService cmisSessionService) {
        this.serverService = serverService;
        this.cmisSessionService = cmisSessionService;
    }

    // 1. List servers
    @GetMapping
    public String listServers(Model model) {
        model.addAttribute("servers", serverService.getAllServers());
        model.addAttribute("cmisConfigs", cmisSessionService.getAll());
        model.addAttribute("content", "server/list");
        model.addAttribute("activeMenu", "server");
        
        return "main";
    }

    // 2. Add new server form
    @GetMapping("/add")
    public String createServerForm(Model model) {
        model.addAttribute("server", new ServerConfig());
        model.addAttribute("serverTypes", ServerType.values());
        model.addAttribute("content", "server/form");
        model.addAttribute("activeMenu", "server");
        
        return "main";
    }

    // 3. Edit server form
    @GetMapping("/edit/{id}")
    public String editServerForm(@PathVariable Long id, Model model) {
        ServerConfig server = serverService.getServerById(id);
        model.addAttribute("server", server);
        model.addAttribute("serverTypes", ServerType.values());
        model.addAttribute("content", "server/form");
        model.addAttribute("activeMenu", "server");
        return "main";
    }

    // 4. Save server (Add/Edit)
    @PostMapping("/save")
    public String saveServer(@ModelAttribute ServerConfig server) {
    	serverService.saveServer(server);
    	return "redirect:" + ROUTES.UI_SERVER;
    }

    // 5. Delete server
    @GetMapping("/delete/{id}")
    public String deleteServer(@PathVariable Long id) {
    	serverService.deleteServer(id);
        return "redirect:" + ROUTES.UI_SERVER;
    }
}
