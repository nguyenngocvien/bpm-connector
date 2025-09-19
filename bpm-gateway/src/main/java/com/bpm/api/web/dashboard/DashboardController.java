package com.bpm.api.web.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.monitor.DashboardService;

@Controller

@RequestMapping(ROUTES.UI)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.getSummary());
        model.addAttribute("content", "dashboard");
        model.addAttribute("activeMenu", "dashboard");
        return "main";
    }
}