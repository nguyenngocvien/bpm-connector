package com.bpm.api.modules.serviceconfig.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.datasource.domain.DataSourceConfig;
import com.bpm.core.datasource.domain.DbServiceConfig;
import com.bpm.core.document.domain.FileServiceConfig;
import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
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

    public ServiceConfigController(ServiceConfigService serviceConfigService) {
    	this.serviceConfigService = serviceConfigService;
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
    	
    	List<DataSourceConfig> datasourceList = serviceConfigService.getAllDataSources();
    	
        model.addAttribute("serviceConfig", config);
        model.addAttribute("datasourceList", datasourceList);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
    	ServiceConfig serviceConfig = serviceConfigService.findById(id);
        List<DataSourceConfig> datasourceList = serviceConfigService.getAllDataSources();

        model.addAttribute("serviceConfig", serviceConfig);
        model.addAttribute("datasourceList", datasourceList);
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
