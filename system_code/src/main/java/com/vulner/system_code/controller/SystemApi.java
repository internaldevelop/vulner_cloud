package com.vulner.system_code.controller;

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
        String runInfo = appName + " is running on port（测试中文）: " + port + ". Register server is: " + defaultZone;
        return runInfo;
    }

    @RequestMapping(value = "/sys_code/run_status", method = RequestMethod.GET)
    @ResponseBody
    public Object runStatus() {
        return ResponseHelper.success(actuatorInfo());
    }
}
