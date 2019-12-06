package com.vulner.system_log.controller;

import com.vulner.system_log.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class SystemApi {
    @Value("${server.port}")
    String port;
    @Value("${spring.application.name}")
    String appName;
    @Value("${myprops.defaultZone}")
    String defaultZone;

    @GetMapping(value = "/actuator/info")
    @ResponseBody
    public Object actuatorInfo() {
        return appName + " is running on port: " + port + ". Register server is: " + defaultZone;
    }

    @GetMapping(value = "/sys_log/run_status")
    @ResponseBody
    public Object runStatus() {
        return ResponseHelper.success(actuatorInfo());
    }
}
