package com.bpm.core.serviceconfig.interfaces;

import com.bpm.core.common.response.Response;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;

public interface ServiceInvoker {
    ServiceType getServiceType();
    Response execute(ServiceConfig config, String params);
}