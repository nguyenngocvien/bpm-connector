package com.bpm.api.modules.document.dto;

public class ExportResponse {
    private String id;
    private String downloadUrl;


    public ExportResponse() {}


    public ExportResponse(String id, String downloadUrl) {
        this.id = id;
        this.downloadUrl = downloadUrl;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getDownloadUrl() {
        return downloadUrl;
    }


    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}