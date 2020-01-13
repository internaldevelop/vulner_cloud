package com.vulner.bend_server.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "unified-auth")
public interface UniAuthService {
    @GetMapping(value = "/uni_auth/run_status")
    String runStatus();

    @GetMapping(value = "/account_manage/self")
    Object getSelfAccountInfo(@RequestParam("access_token") String access_token);

}
