package com.vulner.aqmp_bus.controller;

import com.vulner.aqmp_bus.global.RabbitConfig;
import com.vulner.aqmp_bus.service.ErrorCodeService;
import com.vulner.aqmp_bus.service.mq.TopicSender;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

//import com.vulner.aqmp_bus.service.mq.Sender;

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

    /**
     * 发消息
     * @param msg
     * @return
     */
    @GetMapping(value = "/mq_bus/send_data")
    @ResponseBody
    public Object sendData(@RequestParam("exchange_name")String exchangeName, @RequestParam("topic")String topic, @RequestParam("msg") String msg) {
        topicSender.send(exchangeName, topic, msg);
        return errorCodeService.runStatus();
    }

    /**
     * 创建交换机
     * @param exchangeName
     * @return
     */
    @GetMapping(value = "/mq_bus/add_exchange")
    @ResponseBody
    public Object addExchange(@RequestParam("exchange_name") String exchangeName) {
        topicSender.addExchange(exchangeName);
        return errorCodeService.runStatus();
    }

    /**
     * 创建队列并绑定到APP交换机
     * @param queueName
     * @return
     */
    @GetMapping(value = "/mq_bus/create_queue")
    @ResponseBody
    public Object createQueue(@RequestParam("queue_name") String queueName) {
        topicSender.addQueueBinding(queueName, RabbitConfig.MAIN_APP_EXCHANGE);   //队列绑定到交换机
        return errorCodeService.runStatus();
    }

    /**
     * 清空队列
     * @param queueName
     * @return
     */
    @GetMapping(value = "/mq_bus/clear_queue")
    @ResponseBody
    public Object clearQueuea(@RequestParam("queue_name") String queueName) {
        topicSender.clearQueue(queueName);
        return errorCodeService.runStatus();
    }

    /**
     * 删除队列
     * @param queueName
     * @return
     */
    @GetMapping(value = "/mq_bus/del_queue")
    @ResponseBody
    public Object delQueuea(@RequestParam("queue_name") String queueName) {
        topicSender.delQueue(queueName);
        return errorCodeService.runStatus();
    }



}
