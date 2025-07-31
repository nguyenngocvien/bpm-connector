package com.bpm.core.service;

import java.util.Map;

import com.bpm.core.model.response.Response;
import com.bpm.core.model.service.ServiceConfig;

public interface ServiceInvoker {
	Response<Object> invoke(ServiceConfig serviceConfig, Map<String, Object> inputParams);
}
