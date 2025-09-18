package com.bpm.core.log.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLog {
    private String serviceCode;
    private String requestData;
    private String mappedRequest;
    private String responseData;
    private int statusCode;
    private int durationMs;
    private LocalDateTime createdAt = LocalDateTime.now();
}
