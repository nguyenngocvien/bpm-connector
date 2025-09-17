package com.bpm.api.modules.serviceconfig.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.common.response.Response;
import com.bpm.core.common.util.JsonUtil;
import com.bpm.core.datasource.domain.DbServiceConfig;
import com.bpm.core.datasource.infrastructure.DbServiceConfigParser;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import com.bpm.core.serviceconfig.service.ServiceConfigService;
import com.bpm.core.serviceconfig.service.ServiceInvoker;

@Controller
@RequestMapping(ROUTES.UI_TESTING)
public class ServiceTestController {

    private final ServiceConfigService service;
    private final Map<String, ServiceInvoker> invokerMap;

    @Autowired
    public ServiceTestController(ServiceConfigService service, Map<String, ServiceInvoker> invokerMap) {
        this.service = service;
        this.invokerMap = invokerMap;
    }

    @GetMapping
    public String showTestPage(@RequestParam(name = "serviceId", required = false) Long serviceId,
                               Model model) {

        model.addAttribute("content", "service/test");
        model.addAttribute("activeMenu", "test");

        model.addAttribute("serviceId", serviceId);
        model.addAttribute("serviceList", service.findAll(null));

        return "main";
    }
    
    @GetMapping("/load-params")
    public String loadParamsByServiceId(@RequestParam("serviceId") Long serviceId, Model model) {
    	ServiceConfig config = service.findById(serviceId);
    	
    	String input = "";
    	
        if (ServiceType.DB.equals(config.getServiceType())) {
			DbServiceConfig dbConfig = config.getDbServiceConfig();
			
			input = DbServiceConfigParser.convertParamArrayToJsonObject(dbConfig.getParamList());
		}
        
        model.addAttribute("inputJson", input);
        return "service/param-textarea :: textarea";
    }

    @PostMapping
    public String executeService(@RequestParam Long serviceId,
                                 @RequestParam String params,
                                 Model model) {

    	System.out.println(">>> Invoking Service ID = " + serviceId);
    	
        ServiceConfig config = service.findById(serviceId);

        String invokerKey = getInvokerKey(config.getServiceType()); // e.g., "dbInvoker"
        ServiceInvoker invoker = invokerMap.get(invokerKey);

        Response<Object> res;
        if (invoker == null) {
            res = Response.error("No invoker found for type: " + config.getServiceType());
        } else {
            try {
                Map<String, Object> paramMap = JsonUtil.toObjectMap(params);
                res = invoker.invoke(config, paramMap);
            } catch (Exception e) {
                res = Response.error("Invalid JSON or invocation failure: " + e.getMessage());
            }
        }
        model.addAttribute("outputJson", res.toString());
        
        return "service/response :: result";
    }

    private String getInvokerKey(ServiceType serviceType) {
        if (serviceType == null) return null;
        switch (serviceType) {
            case DB: return "dbInvoker";
            case REST: return "restInvoker";
            default: return null;
        }
    }
}
