package com.bpm.core.serviceconfig.service;

import java.util.Map;

import com.bpm.core.common.response.Response;
import com.bpm.core.common.util.JsonUtil;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.service.impl.DBInvoker;
import com.bpm.core.serviceconfig.service.impl.RESTInvoker;

public class ServiceDispatcher {

    private final DBInvoker dbInvoker;
    private final RESTInvoker restInvoker;

    private final ServiceConfigService service;

    public ServiceDispatcher(DBInvoker dbInvoker,
                             RESTInvoker restInvoker,
                             ServiceConfigService service) {
        this.dbInvoker = dbInvoker;
        this.restInvoker = restInvoker;
        
        this.service = service;
    }

    public Response<Object> execute(Long serviceId, String params) {
        ServiceConfig config = service.findById(serviceId);
        Response<Object> res;

        try {
            Map<String, Object> paramMap = JsonUtil.toObjectMap(params);

            switch (config.getServiceType()) {
                case DB:
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

