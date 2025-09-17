package com.bpm.core.datasource.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceStatus {
	private boolean connected;
    private long latency;
    private int activeConnections;
}