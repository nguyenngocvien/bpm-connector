package com.bpm.api.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.common.response.Response;
import com.bpm.core.serviceconfig.service.ServiceDispatcher;

@RestController
@RequestMapping(ROUTES.SERVICE)
public class ServiceController {

    private final ServiceDispatcher dispatcher;

    @Autowired
    public ServiceController(ServiceDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping
    public ResponseEntity<Response> execute(
            @RequestParam String serviceCode,
            @RequestParam String params) {
        try {
            Response response = dispatcher.execute(serviceCode, params);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Response.error("Invalid arguments: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(Response.error("Failed to send email: " + ex.getMessage()));
        }
    }

}
