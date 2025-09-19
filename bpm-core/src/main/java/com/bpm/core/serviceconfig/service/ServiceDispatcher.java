package com.bpm.core.serviceconfig.service;

import java.util.Map;

import com.bpm.core.common.response.Response;
import com.bpm.core.common.util.JsonUtil;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.service.impl.DbInvoker;
import com.bpm.core.serviceconfig.service.impl.RestInvoker;

public class ServiceDispatcher {
	
    private final ServiceConfigRepositoryService service;
    private final DbInvoker dbInvoker;
    private final RestInvoker restInvoker;
    
    public ServiceDispatcher(ServiceConfigRepositoryService service,
				    		DbInvoker dbInvoker,
				            RestInvoker restInvoker) {
        
    	this.dbInvoker = dbInvoker;
        this.restInvoker = restInvoker;
        
        this.service = service;
    }

    public Response<Object> executeServiceById(Long serviceId, String params) {
        ServiceConfig config = service.findById(serviceId);
        return execute(config, params);
    }
    
    public Response<Object> executeServiceByCode(String serviceCode, String params) {
        ServiceConfig config = service.findByCode(serviceCode);
        return execute(config, params);
    }
    
    private Response<Object> execute(ServiceConfig config, String params) {
    	Response<Object> res;

        try {
            Map<String, Object> paramMap = JsonUtil.toObjectMap(params);

            switch (config.getServiceType()) {
                case SQL:
                    res = dbInvoker.invoke(config, paramMap);
                    break;
                case REST:
                    res = restInvoker.invoke(config, paramMap);
                    break;
//                case SOAP:
//                    res = soapInvoker.invoke(config, paramMap);
//                    break;
//                case MAIL:
//                    res = mailInvoker.invoke(config, paramMap);
//                    break;
//                case FILE:
//                    res = fileInvoker.invoke(config, paramMap);
//                    break;
                default:
                    res = Response.error("Unsupported service type: " + config.getServiceType());
            }
        } catch (Exception e) {
            res = Response.error("Invalid JSON or invocation failure: " + e.getMessage());
        }

        return res;
    }
}

