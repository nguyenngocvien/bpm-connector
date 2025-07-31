package com.bpm.api.controller.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.response.Response;
import com.bpm.core.model.service.ServiceConfig;
import com.bpm.core.repository.ServiceConfigRepository;
import com.bpm.core.service.ServiceInvoker;
import com.bpm.core.util.JsonUtil;

@Controller
@RequestMapping(ROUTES.UI_TESTING)
public class ServiceTestController {
	
	private final ServiceConfigRepository repository;
	private final Map<String, ServiceInvoker> invokerMap;

	@Autowired
	public ServiceTestController(ServiceConfigRepository repository, Map<String, ServiceInvoker> invokerMap) {
	    this.repository = repository;
	    this.invokerMap = invokerMap;
	}

	@GetMapping
	public String showTestPage(@RequestParam(name = "serviceCode", required = false) String serviceCode,
	                           @RequestParam(name = "serviceType", required = false) String serviceType,
	                           Model model) {

	    model.addAttribute("content", "service/test");
	    model.addAttribute("activeMenu", "test");
	    model.addAttribute("serviceCode", serviceCode);
	    model.addAttribute("serviceType", serviceType);
	    return "main";
	}

	@PostMapping
    public String executeService(@RequestParam String serviceType,
                                 @RequestParam String serviceCode,
                                 @RequestParam String params,
                                 Model model) {

        ServiceConfig config = repository.findByCode(serviceCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Code"));

        String invokerKey = getInvokerKey(serviceType); // e.g., "dbInvoker"
        ServiceInvoker invoker = invokerMap.get(invokerKey);

        if (invoker == null) {
            model.addAttribute("outputJson", "No invoker found for type: " + serviceType);
            return "service/response :: result";
        }

        Response<Object> res;
        try {
            Map<String, Object> paramMap = JsonUtil.toObjectMap(params);
            res = invoker.invoke(config, paramMap);
        } catch (Exception e) {
            res = Response.error("Invalid JSON or failure");
        }

        model.addAttribute("outputJson", res.toString());
        return "service/response :: result";
    }

    private String getInvokerKey(String serviceType) {
        switch (serviceType.toUpperCase()) {
            case "DB": return "dbInvoker";
            case "REST": return "restInvoker";
            default: return null;
        }
    }
}
