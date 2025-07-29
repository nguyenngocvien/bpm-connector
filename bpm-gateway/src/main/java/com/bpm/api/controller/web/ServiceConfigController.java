package com.bpm.api.controller.web;

import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.repository.ServiceConfigRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/serviceconfig")
public class ServiceConfigController {

    private final ServiceConfigRepository service;

    public ServiceConfigController(ServiceConfigRepository service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<ServiceConfig> configs = service.findAll();
        model.addAttribute("configs", configs);
        return "serviceconfig_list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("serviceConfig", new ServiceConfig());
        return "serviceconfig_form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        ServiceConfig config = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("serviceConfig", config);
        return "serviceconfig_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ServiceConfig config) {
        service.insert(config);
        return "redirect:/serviceconfig/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/serviceconfig/list";
    }
}
