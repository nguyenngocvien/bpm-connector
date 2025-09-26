package com.bpm.api.web.cmis.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.cmis.domain.CmisSessionConfig;
import com.bpm.core.cmis.helper.CmisHelper;
import com.bpm.core.cmis.service.CmisSessionConfigService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ROUTES.UI_CMIS)
public class CmisSessionConfigController {

    private final CmisSessionConfigService cmisService;
    private final AuthRepositoryService authService;

    public CmisSessionConfigController(CmisSessionConfigService cmisService,
                                       AuthRepositoryService authService) {
        this.cmisService = cmisService;
        this.authService = authService;
    }

    // List all configs
//    @GetMapping
//    public String list(Model model) {
//        model.addAttribute("configs", cmisService.getAll());
//        return "cmis/list"; // => templates/cmis/list.html
//    }

    // Add new
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("cmis", new CmisSessionConfig());
        model.addAttribute("authConfigs", authService.getAllAuthConfigs());
        model.addAttribute("content", "cmis/form");
        model.addAttribute("activeMenu", "server");
        
        return "main";
    }

    // Edit
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        CmisSessionConfig config = cmisService.getById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy config id=" + id));

        model.addAttribute("cmis", config);
        model.addAttribute("authConfigs", authService.getAllAuthConfigs());        
        model.addAttribute("content", "cmis/form");
        model.addAttribute("activeMenu", "server");
        return "main";
    }

    // Save
    @PostMapping("/save")
    public String save(@ModelAttribute("cmis") CmisSessionConfig config) {
        cmisService.save(config);
        return "redirect:" + ROUTES.UI_SERVER;
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        cmisService.delete(id);
        return "redirect:" + ROUTES.UI_SERVER;
    }
    
    @PostMapping("/test")
    @ResponseBody
    public Map<String, Object> testConnection(@ModelAttribute CmisSessionConfig config) {
        Map<String, Object> result = new HashMap<>();
        try {
            AuthConfig authConfig = authService.getAuthConfigById(config.getAuthId());

            CmisHelper helper = new CmisHelper();
            helper.connect(
                    config.getAtompubUrl(),
                    authConfig.getUsername(),
                    authConfig.getPassword(),
                    config.getRepositoryId()
            );

            result.put("success", true);
            result.put("message", "✅ Connected successfully!");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "❌ Connection failed: " + e.getMessage());
        }
        return result;
    }

}
