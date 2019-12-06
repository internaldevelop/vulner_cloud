package com.vulner.bend_server.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "system-code")
public interface ErrorCodeService {
    @GetMapping(value = "/sys_code/run_status")
    String runStatus();
}
