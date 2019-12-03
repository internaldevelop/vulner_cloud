package com.vulner.bend_server.controller;

import com.vulner.bend_server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
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

    @ResponseBody
    @RequestMapping(value = "/actuator/info", method = RequestMethod.GET)
    public Object actuatorInfo() {
        return appName + " is running on port: " + port + ". Register server is: " + defaultZone;
    }

    @Autowired
    ErrorCodeService errorCodeService;
    @RequestMapping(value = "/echo/errcode", method = RequestMethod.GET)
    @ResponseBody
    public Object echoErrorCode() {
        return errorCodeService.runStatus();
    }

    @Autowired
    FirmwareFetchService fwFetchService;
    @RequestMapping(value = "/echo/fwfetch", method = RequestMethod.GET)
    @ResponseBody
    public Object echoFwFetch() {
        return fwFetchService.runStatus();
    }

    @Autowired
    FirmwareAnalyzeService fwAnalyzeService;
    @RequestMapping(value = "/echo/fwanalyze", method = RequestMethod.GET)
    @ResponseBody
    public Object echoFwAnalyze() {
        return fwAnalyzeService.runStatus();
    }

    @Autowired
    MqBusService mqBusService;
    @RequestMapping(value = "/echo/mqbus", method = RequestMethod.GET)
    @ResponseBody
    public Object echoMqBus() {
        return mqBusService.runStatus();
    }

    @Autowired
    SysLogService sysLogService;
    @RequestMapping(value = "/echo/syslog", method = RequestMethod.GET)
    @ResponseBody
    public Object echoSysLog() {
        return sysLogService.runStatus();
    }

    @Autowired
    UniAuthService uniAuthService;
    @RequestMapping(value = "/echo/uniauth", method = RequestMethod.GET)
    @ResponseBody
    public Object echoUniAuth() {
        return uniAuthService.runStatus();
    }
}
