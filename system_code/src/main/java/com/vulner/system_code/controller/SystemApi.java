package com.vulner.system_code.controller;

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
        String runInfo = appName + " is running on port: " + port + ". Register server is: " + defaultZone;
        return runInfo;
    }

    @GetMapping(value = "/sys_code/run_status")
    @ResponseBody
    public Object runStatus() {
        return ResponseHelper.success(actuatorInfo());
    }
}
