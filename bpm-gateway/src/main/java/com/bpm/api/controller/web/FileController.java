package com.bpm.api.controller.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.fncmis.UploadForm;
import com.bpm.core.service.CmisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@Controller
@RequestMapping(ROUTES.UI_FILE)
public class FileController {

    private final CmisService cmisService;
    private final ObjectMapper objectMapper;

    @Value("${app.ui.defaultFolderPath:/}")
    private String defaultFolderPath;

    @Value("${app.cmis.defaultObjectTypeId:cmis:document}")
    private String defaultObjectTypeId;

    public FileController(CmisService cmisService, ObjectMapper objectMapper) {
        this.cmisService = cmisService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        UploadForm form = new UploadForm();
        form.setFolderPath(defaultFolderPath);
        form.setObjectTypeId(defaultObjectTypeId);
        model.addAttribute("form", form);
        model.addAttribute("content", "file/upload");
        model.addAttribute("activeMenu", "file");
        return "main";
    }

    @PostMapping("/upload")
    public String handleUpload(@Valid @ModelAttribute("form") UploadForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "main";
        }
        try {
            Map<String, Object> customProps = null;
            if (form.getJsonProps() != null && !form.getJsonProps().isBlank()) {
                customProps = objectMapper.readValue(form.getJsonProps(), new TypeReference<>() {});
            }
            String id = cmisService.upload(form.getFile(), form.getFolderPath(), form.getObjectTypeId(), customProps);
            model.addAttribute("success", "Uploaded successfully. Document ID: " + id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        
        model.addAttribute("content", "file/upload");
        model.addAttribute("activeMenu", "file");
        
        return "main";
    }
}