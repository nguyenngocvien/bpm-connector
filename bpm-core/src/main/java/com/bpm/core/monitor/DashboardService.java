package com.bpm.core.monitor;

public class DashboardService {

    public DashboardSummary getSummary() {
        DashboardSummary summary = new DashboardSummary();

        // Fake data (sau này lấy thật từ DB, log, monitoring system)
        summary.setTotalDatasources(5);
        summary.setDatasourceSuccess(4);
        summary.setDatasourceFailed(1);

        summary.setTotalServers(3);
        summary.setServerSuccess(3);
        summary.setServerFailed(0);

        summary.setTotalServices(10);
        summary.setServiceActive(9);
        summary.setServiceFailed(1);

        return summary;
    }
}
