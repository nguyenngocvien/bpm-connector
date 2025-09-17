package com.bpm.api.modules.document.dto;

public class ExportRequest {
    private String filename;
    private String format;
    private String contentBase64;

    public ExportRequest() {}

    public ExportRequest(String filename, String format, String contentBase64) {
        this.filename = filename;
        this.format = format;
        this.contentBase64 = contentBase64;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getContentBase64() {
        return contentBase64;
    }

    public void setContentBase64(String contentBase64) {
        this.contentBase64 = contentBase64;
    }
}