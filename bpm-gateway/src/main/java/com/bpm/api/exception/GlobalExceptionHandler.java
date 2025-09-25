package com.bpm.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import com.bpm.core.common.response.Response;

import java.time.format.DateTimeParseException;

import com.bpm.core.common.exception.ServiceExecutionException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<Response> buildResponse(HttpStatus status, String code, String message, Long logId) {
	    String data = (logId != null ? logId.toString() : null);
	    Response response = Response.error(status.value(), code, message, data);
	    return ResponseEntity.status(status).body(response);
	}

    // ✅ Xử lý ServiceExecutionException
    @ExceptionHandler(ServiceExecutionException.class)
    public ResponseEntity<Response> handleServiceExecutionException(ServiceExecutionException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return buildResponse(
                status,
                ex.getCode(),
                ex.getMessage(),
                ex.getLogId()
        );
    }

    // ❌ External API timeout
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Response> handleTimeout(ResourceAccessException ex) {
        return buildResponse(HttpStatus.GATEWAY_TIMEOUT, "ERR_TIMEOUT", "External API timeout", null);
    }

    // ❌ External API HTTP error (400, 500…)
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Response> handleHttpStatusCodeException(HttpStatusCodeException ex) {
        return buildResponse(
        		(HttpStatus) ex.getStatusCode(),
        		"ERR_HTTP",
                "External API error: " + ex.getStatusText() + ". " + ex.getResponseBodyAsString(),
                null
        		);
    }

    // ❌ Validation @Valid failed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidation(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, "ERR_VALIDATION", "Validation error: " + errorMsg, null);
    }

    // ❌ Invalid format date/time
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Response> handleDateTimeParse(DateTimeParseException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "ERR_DATE", "Invalid date format" + ex.getParsedString(), null);
    }

    // ❌ Catch all - fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERR_GENERIC", "Unexpected error: " + ex.getMessage(), null);
    }
}