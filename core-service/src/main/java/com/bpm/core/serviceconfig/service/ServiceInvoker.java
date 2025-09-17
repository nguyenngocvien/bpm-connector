package com.bpm.core.serviceconfig.service;

import java.util.Map;

import com.bpm.core.common.response.Response;
import com.bpm.core.serviceconfig.domain.ServiceConfig;

public interface ServiceInvoker {
	Response<Object> invoke(ServiceConfig serviceConfig, Map<String, Object> inputParams);
}
