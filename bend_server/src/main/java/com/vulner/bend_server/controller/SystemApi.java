package com.vulner.bend_server.controller;

import com.alibaba.fastjson.JSON;
import com.vulner.bend_server.service.*;
import com.vulner.bend_server.service.logger.SysLogService;
import com.vulner.bend_server.service.logger.SysLogger;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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
    @Autowired
    SysLogger sysLogger;

    @GetMapping(value = "/echo/errcode")
    @ResponseBody
    public Object echoErrorCode() {
        Object response = errorCodeService.runStatus();
        ResponseBean responseBean = JSON.parseObject(JSON.toJSONString(response), ResponseBean.class);
        if (ResponseHelper.isSuccess(responseBean)) {
            sysLogger.success("echoErrorCode", JSON.toJSONString(responseBean.getPayload()), "");
        } else {
            sysLogger.fail("echoErrorCode", "获取错误码服务状态失败", JSON.toJSONString(responseBean));
        }
        return response;
    }

    @Autowired
    FirmwareFetchService fwFetchService;
    @GetMapping(value = "/echo/fwfetch")
    @ResponseBody
    public Object echoFwFetch() {
        return fwFetchService.runStatus();
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

    @Autowired
    SystemService systemService;

    @GetMapping(value = "/echo/version")
    public @ResponseBody Object getVersion() {
        return systemService.getHostSystemInfo();
    }

    @GetMapping(value = "/starttask/resources")
    @ResponseBody
    public Object startTaskResources(@RequestParam("asset_uuid") String assetUuid, @RequestParam("types") String types, @RequestParam( value = "second_time", required = false) String secondTime) {
        return systemService.startTaskResources(assetUuid, types, secondTime);
    }

    @GetMapping(value = "/resources/setdata")
    @ResponseBody
    public Object setResourcesData(@RequestParam("asset_uuid") String assetUuid, @RequestParam("datas") String datas) {
        return systemService.setResourcesData(assetUuid, datas);
    }

    @GetMapping(value = "/sysinfo/about")
    @ResponseBody
    public Object getSystemAboutInfo() {
        return systemService.getHostSystemInfo();
    }
}
