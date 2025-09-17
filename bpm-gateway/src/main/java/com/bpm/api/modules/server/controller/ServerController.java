package com.bpm.api.modules.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.server.Server;
import com.bpm.core.repository.Store;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ROUTES.UI_SERVER)
public class ServerController {

    private final Store store;

    public ServerController(Store store) {
        this.store = store;
    }

    // 1. List servers
    @GetMapping
    public String listServers(Model model) {
        model.addAttribute("servers", store.servers().findAll());
        
        model.addAttribute("content", "server/list");
        model.addAttribute("activeMenu", "server");
        
        return "main";
    }

    // 2. Add new server form
    @GetMapping("/add")
    public String createServerForm(Model model) {
        model.addAttribute("server", new Server());
        model.addAttribute("content", "server/form");
        model.addAttribute("activeMenu", "server");
        
        return "main";
    }

    // 3. Edit server form
    @GetMapping("/edit/{id}")
    public String editServerForm(@PathVariable Long id, Model model) {
        Server server = store.servers().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid server ID: " + id));
        model.addAttribute("server", server);
        
        model.addAttribute("content", "server/form");
        model.addAttribute("activeMenu", "server");
        return "main";
    }

    // 4. Save server (Add/Edit)
    @PostMapping("/save")
    public String saveServer(@ModelAttribute Server server) {
    	store.servers().save(server);
    	return "redirect:" + ROUTES.UI_SERVER;
    }

    // 5. Delete server
    @GetMapping("/delete/{id}")
    public String deleteServer(@PathVariable Long id) {
    	store.servers().delete(id);
        return "redirect:" + ROUTES.UI_SERVER;
    }
}
