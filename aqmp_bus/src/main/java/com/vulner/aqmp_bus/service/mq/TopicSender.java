package com.vulner.aqmp_bus.service.mq;

import com.vulner.aqmp_bus.global.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发消息
     * @param topic
     * @param msg
     */
    public void send(String topic, String msg) {
        logger.info("---> send MQ Topic: " + topic);
        logger.info("---> send message: " + msg);
        this.rabbitTemplate.convertAndSend(RabbitConfig.MAIN_EXCHANGE, topic, msg);
    }

    public void sendMainTopic(String msg) {
        send(RabbitConfig.DEFAULT_TOPIC, msg);
    }

    /**
     * 广播发消息
     * @param msg
     */
    public void sendFanout(Object msg) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.MAIN_FANOUT_EXCHANGE,null, msg, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            // 设置这条消息的过期时间
            messageProperties.setExpiration("5000");
            return message;
        });
    }

    public RabbitAdmin rabbitAdmin() {
        return  new RabbitAdmin(rabbitTemplate);
    }

    /**
     * 创建Exchange
     * @param exchangeName
     */
    public void addExchange(String exchangeName){
        rabbitAdmin().declareExchange(new TopicExchange(exchangeName));
    }

    /**
     * 创建队列
     * @param queueName
     * @return
     */
    public String addQueue(String queueName) {
        return rabbitAdmin().declareQueue(new Queue(queueName));
    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     * @param queueName
     */
    public void addQueueBinding(String queueName, String exchangeName){
        addQueue(queueName);  // 创建队列
        Binding binding = BindingBuilder.bind(new Queue(queueName)).to(new TopicExchange(exchangeName)).with(queueName);  // 队列绑定到交换机
        rabbitAdmin().declareBinding(binding);
    }

    /**
     *  删除队列
     * @param queueName
     */
    public void delQueue(String queueName) {
        rabbitAdmin().deleteQueue(queueName);
    }

    /**
     * 清空队列
     * @param queueName
     */
    public void clearQueue(String queueName) {
        rabbitAdmin().purgeQueue(queueName);
    }

}
