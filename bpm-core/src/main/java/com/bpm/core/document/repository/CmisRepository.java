package com.bpm.core.document.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bpm.core.document.domain.CreateDocRequest;
import com.bpm.core.document.domain.CreateDocResponse;

public class CmisRepository {

    private final Session session;

    public CmisRepository(Session session) {
        this.session = session;
    }

    public Folder getOrCreateFolderByPath(String folderPath) {
        if (folderPath == null || folderPath.isBlank()) {
            throw new IllegalArgumentException("Folder path is required");
        }
        try {
            CmisObject obj = session.getObjectByPath(folderPath);
            if (obj instanceof Folder) {
                return (Folder) obj;
            }
            throw new IllegalStateException("Path exists but is not a folder: " + folderPath);
        } catch (Exception notFound) {
            // create recursively
            String[] parts = folderPath.split("/");
            StringBuilder currentPath = new StringBuilder();
            Folder current = session.getRootFolder();
            for (String p : parts) {
                if (p == null || p.isBlank()) continue;
                currentPath.append("/").append(p);
                Folder next;
                try {
                    CmisObject existing = session.getObjectByPath(currentPath.toString());
                    if (existing instanceof Folder) {
                        next = (Folder) existing;
                    } else {
                        throw new IllegalStateException("Non-folder segment: " + currentPath);
                    }
                } catch (Exception e) {
                    Map<String, Object> props = new HashMap<>();
                    props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
                    props.put(PropertyIds.NAME, p);
                    next = current.createFolder(props);
                }
                current = next;
            }
            return current;
        }
    }
    
    public CreateDocResponse createDocument(CreateDocRequest req) {
        Folder folder = getOrCreateFolderByPath(req.getFolderPath());

        // Merge properties
        Map<String, Object> props = new HashMap<>();
        props.put(PropertyIds.OBJECT_TYPE_ID, StringUtils.hasText(req.getTypeId()) ? req.getTypeId() : "cmis:document");
        props.put(PropertyIds.NAME, req.getName());
        if (req.getProperties() != null) {
            props.putAll(req.getProperties());
        }

        // Create content stream
        byte[] bytes = Base64.getDecoder().decode(req.getBase64Content());
        ContentStream content = session.getObjectFactory().createContentStream(
                req.getName(),
                bytes.length,
                req.getMimeType(),
                new ByteArrayInputStream(bytes)
        );

        // Create document (Major version)
        Document doc = folder.createDocument(props, content, VersioningState.MAJOR);

        return new CreateDocResponse(
        	    doc.getId(),
        	    doc.getName(),
        	    doc.getType().getId(),
        	    Optional.ofNullable(doc.getVersionLabel()).orElse(""),
        	    doc.getPaths(),
        	    doc.getCreatedBy(),
        	    doc.getCreationDate() != null ? doc.getCreationDate().getTime() : null, // <-- convert
        	    doc.getLastModifiedBy()
        	);
    }

    public String createDocument(MultipartFile file, String folderPath, String objectTypeId, Map<String, Object> customProps) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        // Default type
        if (objectTypeId == null || objectTypeId.isBlank()) {
            objectTypeId = "cmis:document";
        }

        Folder folder = getOrCreateFolderByPath(folderPath);
        
        String safeName = generateSafeName(folder, file.getOriginalFilename());

        Map<String, Object> props = new HashMap<>();
        props.put(PropertyIds.OBJECT_TYPE_ID, objectTypeId);
        props.put(PropertyIds.NAME, safeName);
        if (customProps != null) {
            props.putAll(customProps);
        }

        byte[] data = file.getBytes();
        ContentStream contentStream = new ContentStreamImpl(
                safeName,
                BigInteger.valueOf(data.length),
                Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"),
                new ByteArrayInputStream(data)
        );

        Document doc = folder.createDocument(props, contentStream, VersioningState.MAJOR);
        return doc.getId();
    }

    private String generateSafeName(Folder folder, String originalName) {
        String safeName = Optional.ofNullable(originalName).orElse("upload.bin");

        for (CmisObject child : folder.getChildren()) {
            if (safeName.equalsIgnoreCase(child.getName())) {
                String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                int dot = safeName.lastIndexOf('.');
                if (dot > 0) {
                    safeName = safeName.substring(0, dot) + "-" + ts + safeName.substring(dot);
                } else {
                    safeName = safeName + "-" + ts;
                }
                break;
            }
        }
        return safeName;
    }
    
    // Utility to parse simple JSON into CMIS props (e.g., {"cmis:objectTypeId":"MyType","MyProp":"abc"})
    public Map<String, Object> parseJsonProps(String json) {
        if (json == null || json.isBlank()) return Collections.emptyMap();
        try {
            // primitive tiny parser to avoid depending on Jackson here; your controller uses Jackson if needed
//            Properties p = new Properties();
            // Not a real JSON parser; replace in controller if you prefer Jackson mapping.
            return Collections.emptyMap();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}