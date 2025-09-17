package com.bpm.core.serviceconfig.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bpm.core.datasource.domain.DatasourceStatus;
import com.bpm.core.mail.domain.MailServerStatus;
import com.bpm.core.serviceconfig.domain.ServiceMetric;

public class HealthCheckService {

    public DatasourceStatus checkDatasource() {
        DatasourceStatus dsStatus = new DatasourceStatus();
        dsStatus.setConnected(true);
        dsStatus.setLatency(150);
        dsStatus.setActiveConnections(10);
        return dsStatus;
    }

    public MailServerStatus checkMailServer() {
        MailServerStatus mailStatus = new MailServerStatus();
        mailStatus.setConnected(true);
        mailStatus.setSuccessRate(95);
        mailStatus.setAvgSendTime(300);
        return mailStatus;
    }

    public List<ServiceMetric> getServiceMetrics() {
        ServiceMetric s1 = new ServiceMetric();
        s1.setName("DB Adapter");
        s1.setStatus("OK");
        s1.setResponseTime(120);
        s1.setErrorRate(0.5);

        ServiceMetric s2 = new ServiceMetric();
        s2.setName("Email Adapter");
        s2.setStatus("WARN");
        s2.setResponseTime(600);
        s2.setErrorRate(5.0);

        return Arrays.asList(s1, s2);
    }
}

