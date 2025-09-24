package com.bpm.core.common.exception;

public class ServiceExecutionException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -354585202905412751L;
	private final int statusCode;
	private final String code;
    private final Long logId;

    public ServiceExecutionException(int statusCode, String code, String message, Long logId) {
        super(message);
        this.statusCode = statusCode;
        this.code = code;
        this.logId = logId;
    }

    public int getStatusCode() {
		return statusCode;
	}
    
    public String getCode() {
        return code;
    }

    public Long getLogId() {
        return logId;
    }
}
	