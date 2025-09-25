package com.bpm.api.web.doctmplt.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.document.domain.DocumentTemplate;
import com.bpm.core.document.service.TemplateRepositoryService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_DOC_TEMPLATE)
public class DocTemplateConfigController {

    private final TemplateRepositoryService service;

    public DocTemplateConfigController(TemplateRepositoryService service) {
        this.service = service;
    }
    
    @GetMapping
    public String list(Model model) {
        List<DocumentTemplate> configs = service.getAllTemplates();
        model.addAttribute("doctemplates", configs);
        model.addAttribute("content", "doc/list");
        model.addAttribute("activeMenu", "templates");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("doctemplate", new DocumentTemplate());
        model.addAttribute("content", "doc/form");
        model.addAttribute("activeMenu", "templates");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
    	DocumentTemplate config = service.getTemplateById(id);
        model.addAttribute("doctemplate", config);
        model.addAttribute("content", "doc/form");
        model.addAttribute("activeMenu", "templates");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DocumentTemplate template,
    					@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
    	
    	if (file != null && !file.isEmpty()) {
            template.setContent(file.getBytes());
        } else if (template.getId() != null) {
            byte[] oldContent = service.getTemplateById(template.getId()).getContent();
            template.setContent(oldContent);
        }
    	
    	service.saveTemplate(template);
        return "redirect:" + ROUTES.UI_DOC_TEMPLATE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
    	service.deleteTemplate(id);
        return "redirect:" + ROUTES.UI_DOC_TEMPLATE;
    }
}
