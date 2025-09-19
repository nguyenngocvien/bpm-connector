package com.bpm.api.web.servicetest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.common.response.Response;
import com.bpm.core.db.domain.DbServiceConfig;
import com.bpm.core.db.infrastructure.DbServiceConfigParser;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import com.bpm.core.serviceconfig.service.ServiceConfigRepositoryService;
import com.bpm.core.serviceconfig.service.ServiceDispatcher;

@Controller
@RequestMapping(ROUTES.UI_TESTING)
public class ServiceTestController {

    private final ServiceConfigRepositoryService service;
    private final ServiceDispatcher dispatcher;

    @Autowired
    public ServiceTestController(ServiceConfigRepositoryService service, ServiceDispatcher dispatcher) {
        this.service = service;
        this.dispatcher = dispatcher;
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
    	
        if (ServiceType.SQL.equals(config.getServiceType())) {
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

        Response<Object> res = dispatcher.execute(serviceId, params);

        model.addAttribute("outputJson", res.toString());
        return "service/response :: result";
    }
    
    @PostMapping
    public String executeService(@RequestParam String serviceCode,
                                 @RequestParam String params,
                                 Model model) {

        System.out.println(">>> Invoking Service Code = " + serviceCode);

        Response<Object> res = dispatcher.execute(serviceCode, params);

        model.addAttribute("outputJson", res.toString());
        return "service/response :: result";
    }
}
