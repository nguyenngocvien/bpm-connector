package com.bpm.core.serviceconfig.interfaces;

import com.bpm.core.common.response.Response;

public interface ServiceInvoker {
	
	Response<Object> execute(Long serviceId, String jsonObject);
	
}
