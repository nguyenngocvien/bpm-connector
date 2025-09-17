package com.bpm.api.modules.auth.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.auth.AuthConfig;
import com.bpm.core.repository.Store;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_AUTH)
public class AuthController {

    private final Store store;

    public AuthController(Store store) {
        this.store = store;
    }

    @GetMapping
    public String list(Model model) {
        List<AuthConfig> authList = store.auths().findAll();
        model.addAttribute("authList", authList);
        model.addAttribute("content", "auth/list");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("authConfig", new AuthConfig());
        model.addAttribute("content", "auth/form");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        AuthConfig authConfig = store.auths().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        authConfig.setPassword(""); // clear password for editing
        model.addAttribute("authConfig", authConfig);
        model.addAttribute("content", "auth/form");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AuthConfig auth) {
    	store.auths().save(auth);
        return "redirect:" + ROUTES.UI_AUTH;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
    	store.auths().deleteById(id);
        return "redirect:" + ROUTES.UI_AUTH;
    }
}