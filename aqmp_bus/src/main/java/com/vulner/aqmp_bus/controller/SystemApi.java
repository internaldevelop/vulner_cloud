package com.vulner.aqmp_bus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vulner.aqmp_bus.global.RabbitConfig;
import com.vulner.aqmp_bus.service.mq.TopicSender;
//import com.vulner.aqmp_bus.service.mq.Sender;
import com.vulner.common.response.ResponseHelper;
import com.vulner.aqmp_bus.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/mq_bus/run_status", method = RequestMethod.GET)
    @ResponseBody
    public Object runStatus() {
        return ResponseHelper.success(actuatorInfo());
    }

    @Autowired
    ErrorCodeService errorCodeService;
    @RequestMapping(value = "/mq_bus/errcode", method = RequestMethod.GET)
    @ResponseBody
    public Object mqbusErrorCode() {
        return errorCodeService.runStatus();
    }

    /**
     * 广播
     */
    @Autowired
    private TopicSender topicSender;
    @GetMapping(value = "/mq_bus/send_fanout_data")
    @ResponseBody
    public Object mqbusSendDataMQ(@RequestParam("msg") Object msg) {
        topicSender.sendFanout(msg);
        return errorCodeService.runStatus();
    }


}
