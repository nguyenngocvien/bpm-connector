package com.bpm.core.service.impl;

import com.bpm.core.constant.SERVICE_TYPE;
import com.bpm.core.entity.Response;
import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.repository.ServiceConfigRepository;
import com.bpm.core.service.ServiceInvoker;
import com.bpm.core.util.JsonUtil;
import com.bpm.core.util.TemplateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceInvokerImpl implements ServiceInvoker {

    private final ServiceConfigRepository configRepository;
    private final RESTInvoker restInvoker;
    private final SQLConnector sqlConnector;

    public ServiceInvokerImpl(ServiceConfigRepository configRepository, RESTInvoker restInvoker, SQLConnector sqlConnector) {
        this.configRepository = configRepository;
        this.restInvoker = restInvoker;
        this.sqlConnector = sqlConnector;
    }

    @Override
    public String invoke(String serviceCode, Map<String, Object> params) {
        try {
            Optional<ServiceConfig> opt = configRepository.findServiceByCode(serviceCode);
            if (!opt.isPresent()) {
                return errorJson("Service not found: " + serviceCode);
            }

            ServiceConfig config = opt.get();
            String type = config.getServiceType();

            Object result;
            switch (type) {
                case SERVICE_TYPE.REST :
                    result = handleRest(config, params);
                    break;
                case SERVICE_TYPE.SOAP:
                    result = handleSoap(config, params);
                    break;
                case SERVICE_TYPE.DB:
                    result = handleDb(config, params);
                    break;
                case SERVICE_TYPE.MAIL:
                    result = handleMail(config, params);
                    break;
                case SERVICE_TYPE.FILE:
                    result = handleFile(config, params);
                    break;
                default:
                    result = errorJson("Unsupported service type: " + type);
                    break;
            }

            return JsonUtil.toString(Response.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            return errorJson("Error invoking service: " + e.getMessage());
        }
    }

    private Object handleRest(ServiceConfig config, Map<String, Object> params) throws Exception {
        String body = TemplateUtil.render(config.getPayloadTemplate(), params);
        return restInvoker.invoke(config, body);
    }

    private Object handleSoap(ServiceConfig config, Map<String, Object> params) {
        return "SOAP call simulated";
    }

    private Object handleDb(ServiceConfig config, Map<String, Object> params) {
        return sqlConnector.invoke(config, params);
    }

    private Object handleMail(ServiceConfig config, Map<String, Object> params) {
        return "Email sent";
    }

    private Object handleFile(ServiceConfig config, Map<String, Object> params) {
        return "/generated/file/path";
    }

    private String errorJson(String msg) {
        try {
        	Map<String, Object> map = new HashMap<>();
        	map.put("code", 1);
        	map.put("message", msg);
            return JsonUtil.toString(map);
        } catch (Exception e) {
            return "{\"code\":1,\"message\":\"" + msg + "\"}";
        }
    }
}
