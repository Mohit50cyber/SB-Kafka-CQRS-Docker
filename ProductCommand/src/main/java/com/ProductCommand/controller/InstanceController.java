package com.ProductCommand.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstanceController {

    @Value("${server.port}")
    private String port;

    private final String instanceId = java.util.UUID.randomUUID().toString();

    @GetMapping("/provider")
    public String getInstanceInfo() {
        System.out.println( "This is the Instance Controller" +
                "Request served from port: PORT : " + port);

        return "Instance served by port: PORT : " + port + " . InstanceId " + instanceId;
    }
}
