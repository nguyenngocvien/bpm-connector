package com.bpm.api.web.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.domain.AuthType;
import com.bpm.core.auth.service.AuthRepositoryService;

import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_AUTH)
public class AuthController {

    private final AuthRepositoryService authRepository;

    public AuthController(AuthRepositoryService authRepository) {
        this.authRepository = authRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<AuthConfig> authList = authRepository.getAllAuthConfigs();
        model.addAttribute("authList", authList);
        model.addAttribute("content", "auth/list");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("authConfig", new AuthConfig());
        model.addAttribute("authTypes", AuthType.values());
        model.addAttribute("content", "auth/form");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        AuthConfig authConfig = authRepository.getAuthConfigById(id);
        authConfig.setPassword(""); // clear password for editing
        model.addAttribute("authConfig", authConfig);
        model.addAttribute("authTypes", AuthType.values());
        model.addAttribute("content", "auth/form");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AuthConfig auth) {
    	authRepository.saveAuthConfig(auth);
        return "redirect:" + ROUTES.UI_AUTH;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
    	authRepository.deleteAuthConfig(id);
        return "redirect:" + ROUTES.UI_AUTH;
    }
}