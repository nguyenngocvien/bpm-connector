package com.bpm.api.service.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.cmis.helper.CmisHelper;
import com.bpm.core.cmis.service.CmisSessionService;
import lombok.RequiredArgsConstructor;

import org.apache.chemistry.opencmis.client.api.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ROUTES.CMIS)
@RequiredArgsConstructor
public class CmisController {

    private final CmisSessionService cmisSessionService;

    @PostMapping("/upload/{serverName}")
    public ResponseEntity<String> upload(
    		@PathVariable String serverName,
            @RequestParam("folderPath") String folderPath,
            @RequestParam("file") MultipartFile file) {

        try {
            CmisHelper helper = cmisSessionService.getOrConnectByName(serverName);

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
    
    /**
     * Download file (auto connect if not exits session cache)
     */
    @GetMapping("/download/{serverName}/{docId}")
    public ResponseEntity<byte[]> download(
            @PathVariable String serverName,
            @PathVariable String docId) {
        try {
            CmisHelper helper = cmisSessionService.getOrConnectByName(serverName);
            byte[] content = helper.downloadDocument(docId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + docId + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Clear session cache (debug/troubleshoot)
     */
    @DeleteMapping("/cache/{serverName}")
    public ResponseEntity<String> clearCache(@PathVariable String configName) {
        cmisSessionService.clearCache(configName);
        return ResponseEntity.ok("Cleared cache for: " + configName);
    }

    @DeleteMapping("/cache")
    public ResponseEntity<String> clearAllCache() {
        cmisSessionService.clearAllCache();
        return ResponseEntity.ok("Cleared all cache");
    }
}
