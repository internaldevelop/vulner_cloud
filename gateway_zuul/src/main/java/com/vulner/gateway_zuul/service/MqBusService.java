package com.vulner.gateway_zuul.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "aqmp-bus")
public interface MqBusService {
    @GetMapping(value = "/mq_bus/run_status")
    String runStatus();
}
