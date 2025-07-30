package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.repository.ServiceConfigRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_SERVICE_CONFIG)
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
        model.addAttribute("serviceConfig", new ServiceConfig());
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        ServiceConfig config = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("serviceConfig", config);
        model.addAttribute("content", "service/form");
        model.addAttribute("activeMenu", "service");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ServiceConfig config) {
        service.save(config);
        return "redirect:" + ROUTES.UI_SERVICE_CONFIG;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:" + ROUTES.UI_SERVICE_CONFIG;
    }
}
