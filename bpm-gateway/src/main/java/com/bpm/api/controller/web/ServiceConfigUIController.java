package com.bpm.api.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.repository.ServiceConfigRepository;

@Controller
@RequestMapping(ROUTES.UI_SERVICE_CONFIG)
public class ServiceConfigUIController {

    @Autowired
    private ServiceConfigRepository repository;

    @GetMapping
    public String list(Model model) {
        List<ServiceConfig> configs = repository.findAll();
        model.addAttribute("configs", configs);
        return "service_list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("config", new ServiceConfig());
        return "service_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ServiceConfig config) {
        if (config.getId() == null) {
            repository.insert(config);
        } else {
            repository.update(config);
        }
        return "redirect:/config";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        ServiceConfig config = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Config not found"));
        model.addAttribute("config", config);
        return "service_form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/config";
    }
}
