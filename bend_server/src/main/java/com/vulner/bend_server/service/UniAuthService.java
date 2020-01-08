package com.vulner.bend_server.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "unified-auth")
public interface UniAuthService {
    @GetMapping(value = "/uni_auth/run_status")
    String runStatus();

    @GetMapping(value = "/account_manage/self")
    Object getSelfAccountInfo();

}
