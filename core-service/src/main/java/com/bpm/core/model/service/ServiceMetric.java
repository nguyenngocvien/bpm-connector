package com.bpm.core.model.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMetric {
    private String name;
    private String status;
    private long responseTime;
    private double errorRate;
}