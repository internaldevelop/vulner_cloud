package com.vulner.gateway_zuul.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "unified-auth")
public interface UniAuthService {
    @GetMapping(value = "/uni_auth/run_status")
    String runStatus();
}
