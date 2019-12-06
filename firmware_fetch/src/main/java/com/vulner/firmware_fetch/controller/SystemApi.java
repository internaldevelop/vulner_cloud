package com.vulner.firmware_fetch.controller;

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

    @RequestMapping(value = "/actuator/info", method = RequestMethod.GET)
    @ResponseBody
    public Object actuatorInfo() {
        return appName + " is running on port: " + port + ". Register server is: " + defaultZone;
    }

    @RequestMapping(value = "/fw_fetch/run_status", method = RequestMethod.GET)
    @ResponseBody
    public Object runStatus() {
        return ResponseHelper.success(actuatorInfo());
    }
}
