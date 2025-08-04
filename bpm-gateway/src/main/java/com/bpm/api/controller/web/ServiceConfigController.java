package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.db.DataSourceConfig;
import com.bpm.core.model.db.DbServiceConfig;
import com.bpm.core.model.file.FileServiceConfig;
import com.bpm.core.model.mail.MailServiceConfig;
import com.bpm.core.model.rest.RestServiceConfig;
import com.bpm.core.model.service.ServiceConfig;
import com.bpm.core.repository.Store;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_SERVICE)
public class ServiceConfigController {

    private final Store store;

    public ServiceConfigController(Store store) {
    	this.store = store;
    }

    @GetMapping
    public String list(Model model) {
        List<ServiceConfig> configs = store.serviceConfigs().findAll();
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
        model.addAttribute("serviceConfig", config);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        ServiceConfig serviceConfig = store.serviceConfigs().findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));

        serviceConfig.setDbServiceConfig(
            store.dbServices().findById(id).orElse(new DbServiceConfig())
        );

        serviceConfig.setRestServiceConfig(
            store.restServices().findById(id).orElse(new RestServiceConfig())
        );

        serviceConfig.setMailServiceConfig(
            store.mailServices().findById(id).orElse(new MailServiceConfig())
        );

        serviceConfig.setFileServiceConfig(
            store.fileServices().findById(id).orElse(new FileServiceConfig())
        );

        List<DataSourceConfig> datasourceList = store.datasources().findAll();

        model.addAttribute("serviceConfig", serviceConfig);
        model.addAttribute("datasourceList", datasourceList);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");

        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("serviceConfig") ServiceConfig config) {
        store.serviceConfigs().save(config);
        return "redirect:" + ROUTES.UI_SERVICE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (!store.serviceConfigs().existsById(id)) {
            // Optional: log warning
            return "redirect:" + ROUTES.UI_SERVICE + "?error=NotFound";
        }
        store.serviceConfigs().deleteById(id);
        return "redirect:" + ROUTES.UI_SERVICE;
    }
}
