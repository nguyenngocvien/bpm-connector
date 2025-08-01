package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.db.DbServiceConfig;
import com.bpm.core.model.file.FileServiceConfig;
import com.bpm.core.model.mail.MailServiceConfig;
import com.bpm.core.model.rest.RestServiceConfig;
import com.bpm.core.model.service.ServiceConfig;
import com.bpm.core.repository.ServiceConfigRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_SERVICE)
public class ServiceConfigController {

    private final ServiceConfigRepository service;

    public ServiceConfigController(ServiceConfigRepository service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        List<ServiceConfig> configs = service.findAll();
        model.addAttribute("serviceConfigs", configs);
        model.addAttribute("content", "service/list");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        ServiceConfig config = ServiceConfig.builder()
                .dbServiceConfig(new DbServiceConfig())
                .restServiceConfig(new RestServiceConfig())
                .mailServiceConfig(new MailServiceConfig())
                .fileServiceConfig(new FileServiceConfig())
                .build();

        model.addAttribute("serviceConfig", config);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        ServiceConfig serviceConfig = service.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));

        //Initial child object if null
        if (serviceConfig.getDbServiceConfig() == null) serviceConfig.setDbServiceConfig(new DbServiceConfig());
        if (serviceConfig.getRestServiceConfig() == null) serviceConfig.setRestServiceConfig(new RestServiceConfig());
        if (serviceConfig.getMailServiceConfig() == null) serviceConfig.setMailServiceConfig(new MailServiceConfig());
        if (serviceConfig.getFileServiceConfig() == null) serviceConfig.setFileServiceConfig(new FileServiceConfig());

        model.addAttribute("serviceConfig", serviceConfig);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("serviceConfig") ServiceConfig config) {
        service.save(config);
        return "redirect:" + ROUTES.UI_SERVICE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (!service.existsById(id)) {
            // Optional: log warning
            return "redirect:" + ROUTES.UI_SERVICE + "?error=NotFound";
        }
        service.deleteById(id);
        return "redirect:" + ROUTES.UI_SERVICE;
    }
}
