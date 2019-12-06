package com.vulner.system_log.controller;

import com.vulner.system_log.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vulner.common.response.ResponseHelper;
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

    @RequestMapping(value = "/sys_log/run_status", method = RequestMethod.GET)
    @ResponseBody
    public Object runStatus() {
        return ResponseHelper.success(actuatorInfo());
    }

    @Autowired
    ErrorCodeService errorCodeService;
    @RequestMapping(value = "/sys_log/errcode", method = RequestMethod.GET)
    @ResponseBody
    public Object mqbusErrorCode() {
        return errorCodeService.runStatus();
    }
}
