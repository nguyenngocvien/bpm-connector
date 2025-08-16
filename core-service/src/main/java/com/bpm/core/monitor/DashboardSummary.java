package com.bpm.core.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummary {
    private int totalDatasources;
    private int datasourceSuccess;
    private int datasourceFailed;

    private int totalServers;
    private int serverSuccess;
    private int serverFailed;

    private int totalServices;
    private int serviceActive;
    private int serviceFailed;
}
