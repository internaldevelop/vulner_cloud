package com.vulner.firmware_analyze.controller;

import com.vulner.firmware_analyze.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/fw_analyze/run_status", method = RequestMethod.GET)
    @ResponseBody
    public Object runStatus() {
        return actuatorInfo();
    }

    @Autowired
    ErrorCodeService errorCodeService;
    @RequestMapping(value = "/fw_analyze/errcode", method = RequestMethod.GET)
    @ResponseBody
    public Object mqbusErrorCode() {
        return errorCodeService.runStatus();
    }
}
