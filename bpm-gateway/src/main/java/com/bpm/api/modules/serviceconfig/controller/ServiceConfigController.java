package com.bpm.api.modules.serviceconfig.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.db.domain.DataSourceConfig;
import com.bpm.core.db.domain.DbServiceConfig;
import com.bpm.core.db.domain.ParamType;
import com.bpm.core.db.domain.SqlType;
import com.bpm.core.db.service.DataSourceService;
import com.bpm.core.document.domain.FileServiceConfig;
import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.server.domain.Server;
import com.bpm.core.server.service.ServerService;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import com.bpm.core.serviceconfig.service.ServiceConfigService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_SERVICE)
public class ServiceConfigController {

	private final ServiceConfigService serviceConfigService;
	private final DataSourceService dataSourceService;
	private final ServerService serverService;

    public ServiceConfigController(ServiceConfigService serviceConfigService, DataSourceService dataSourceService, ServerService serverService) {
    	this.serviceConfigService = serviceConfigService;
    	this.dataSourceService = dataSourceService;
    	this.serverService = serverService;
    }

    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
    	
    	List<ServiceConfig> configs = serviceConfigService.findAll(keyword);
        
        model.addAttribute("keyword", keyword);
        model.addAttribute("serviceConfigs", configs);
        model.addAttribute("content", "service/list");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
    	ServiceConfig config = new ServiceConfig();
    	config.setDbServiceConfig(new DbServiceConfig());
    	config.setRestServiceConfig(new RestServiceConfig());
    	config.setMailServiceConfig(new MailServiceConfig());
    	config.setFileServiceConfig(new FileServiceConfig());
    	
    	List<DataSourceConfig> datasourceList = dataSourceService.getAllDataSources();
    	List<Server> servers = serverService.getAllServers();
    	
    	model.addAttribute("serviceTypes", ServiceType.values());
    	model.addAttribute("sqlTypes", SqlType.values());
    	model.addAttribute("paramTypes", ParamType.values());
    	
        model.addAttribute("serviceConfig", config);
        model.addAttribute("datasourceList", datasourceList);
        model.addAttribute("servers", servers);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
    	ServiceConfig serviceConfig = serviceConfigService.findById(id);
        List<DataSourceConfig> datasourceList = dataSourceService.getAllDataSources();
        List<Server> servers = serverService.getAllServers();

        model.addAttribute("serviceTypes", ServiceType.values());
        model.addAttribute("sqlTypes", SqlType.values());
        model.addAttribute("paramTypes", ParamType.values());
        
        model.addAttribute("serviceConfig", serviceConfig);
        model.addAttribute("datasourceList", datasourceList);
        model.addAttribute("servers", servers);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");

        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("serviceConfig") ServiceConfig config) {
    	serviceConfigService.save(config);
        return "redirect:" + ROUTES.UI_SERVICE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
    	serviceConfigService.delete(id);
        return "redirect:" + ROUTES.UI_SERVICE;
    }
    

    @PostMapping("/{id}/toggle-log")
    public ResponseEntity<?> toggleLog(@PathVariable Long id, @RequestParam boolean enabled) {
    	serviceConfigService.toggleLog(id, enabled);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/toggle-active")
    public ResponseEntity<?> toggleActive(@PathVariable Long id, @RequestParam boolean active) {
    	serviceConfigService.toggleActive(id, active);
        return ResponseEntity.ok().build();
    }
}
