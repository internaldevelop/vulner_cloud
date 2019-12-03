package com.vulner.bend_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@FeignClient(value = "system-log")
public interface SysLogService {
    @RequestMapping(value = "/sys_log/run_status", method = RequestMethod.GET)
    String runStatus();
}
