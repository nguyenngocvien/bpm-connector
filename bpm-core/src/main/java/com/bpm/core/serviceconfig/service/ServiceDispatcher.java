package com.bpm.core.serviceconfig.service;

import com.bpm.core.common.response.Response;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.service.impl.DbExecutor;
import com.bpm.core.serviceconfig.service.impl.EmailSender;
import com.bpm.core.serviceconfig.service.impl.RestInvoker;

public class ServiceDispatcher{
	
    private final ServiceConfigRepositoryService service;
    private final DbExecutor dbExecutor;
    private final RestInvoker restInvoker;
    private final EmailSender emailSender;
    
    public ServiceDispatcher(ServiceConfigRepositoryService service,
				    		DbExecutor dbExecutor,
				            RestInvoker restInvoker,
				            EmailSender emailSender) {
        
    	this.service = service;
    	this.dbExecutor = dbExecutor;
        this.restInvoker = restInvoker;
        this.emailSender = emailSender;   
    }

    public Response<Object> execute(Long serviceId, String params) {
        ServiceConfig config = service.findById(serviceId);
        return execute(config, params);
    }
    
    public Response<Object> execute(String serviceCode, String params) {
        ServiceConfig config = service.findByCode(serviceCode);
        return execute(config, params);
    }
    
    private Response<Object> execute(ServiceConfig config, String params) {
    	Response<Object> res;

        try {

            switch (config.getServiceType()) {
                case SQL:
                    res = dbExecutor.execute(config.getId(), params);
                    break;
                case REST:
                    res = restInvoker.invoke(config.getId(), params);
                    break;
                case MAIL:
                    res = emailSender.sendEmail(config.getId(), params);
                    break;
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

