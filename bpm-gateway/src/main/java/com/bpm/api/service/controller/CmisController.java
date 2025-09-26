package com.bpm.api.service.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.cmis.helper.CmisHelper;
import com.bpm.core.cmis.service.CmisSessionService;
import lombok.RequiredArgsConstructor;
import org.apache.chemistry.opencmis.client.api.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ROUTES.CMIS)
@RequiredArgsConstructor
public class CmisController {

    private final CmisSessionService cmisSessionService;

    @PostMapping("/connect/{name}")
    public ResponseEntity<String> connect(@PathVariable String name) {
        cmisSessionService.connectByName(name);
        return ResponseEntity.ok("Connected to CMIS with config: " + name);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("folderPath") String folderPath,
            @RequestParam("file") MultipartFile file) {

        try {
            CmisHelper helper = cmisSessionService.getHelper();

            Document doc = helper.uploadDocument(
                    folderPath,
                    file.getOriginalFilename(),
                    file.getBytes(),
                    file.getContentType() != null ? file.getContentType() : "application/octet-stream"
            );

            return ResponseEntity.ok("Uploaded: " + doc.getName() + " (id=" + doc.getId() + ")");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
