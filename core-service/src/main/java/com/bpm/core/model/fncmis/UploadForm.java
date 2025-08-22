package com.bpm.core.model.fncmis;

import org.springframework.web.multipart.MultipartFile;

public class UploadForm {

    private MultipartFile file;

//    @NotBlank
    private String folderPath;

    private String objectTypeId; // cmis:document or custom class mapping

    private String jsonProps; // JSON key-value for custom props

    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }

    public String getFolderPath() { return folderPath; }
    public void setFolderPath(String folderPath) { this.folderPath = folderPath; }

    public String getObjectTypeId() { return objectTypeId; }
    public void setObjectTypeId(String objectTypeId) { this.objectTypeId = objectTypeId; }

    public String getJsonProps() { return jsonProps; }
    public void setJsonProps(String jsonProps) { this.jsonProps = jsonProps; }
}