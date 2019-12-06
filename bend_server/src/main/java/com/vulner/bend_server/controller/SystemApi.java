package com.vulner.bend_server.controller;

import com.vulner.bend_server.service.*;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;

@RestController
public class SystemApi {
    @Value("${server.port}")
    String port;
    @Value("${spring.application.name}")
    String appName;
    @Value("${myprops.defaultZone}")
    String defaultZone;

    @ResponseBody
    @GetMapping(value = "/actuator/info")
    public Object actuatorInfo() {
        String runStatus = appName + " is running on port: " + port + ". Register server is: " + defaultZone;
        return runStatus;
    }

    @Autowired
    ErrorCodeService errorCodeService;
    @Autowired
    SysLogService sysLogService;

    @GetMapping(value = "/echo/errcode")
    @ResponseBody
    public Object echoErrorCode() {
        sysLogService.addLog("back-end-server", "123", 2, "echoErrorCode", "echoErrorCode", "");
        return errorCodeService.runStatus();
    }

    @Autowired
    FirmwareFetchService fwFetchService;
    @GetMapping(value = "/echo/fwfetch")
    @ResponseBody
    public Object echoFwFetch() {
        return fwFetchService.runStatus();
    }

    @Autowired
    FirmwareAnalyzeService fwAnalyzeService;
    @GetMapping(value = "/echo/fwanalyze")
    @ResponseBody
    public Object echoFwAnalyze() {
        return fwAnalyzeService.runStatus();
    }

    @Autowired
    MqBusService mqBusService;
    @GetMapping(value = "/echo/mqbus")
    @ResponseBody
    public Object echoMqBus() {
        return mqBusService.runStatus();
    }

    @GetMapping(value = "/echo/syslog")
    @ResponseBody
    public Object echoSysLog() {
        return sysLogService.runStatus();
    }

    @Autowired
    UniAuthService uniAuthService;
    @GetMapping(value = "/echo/uniauth")
    @ResponseBody
    public Object echoUniAuth() {
        return uniAuthService.runStatus();
    }
}
