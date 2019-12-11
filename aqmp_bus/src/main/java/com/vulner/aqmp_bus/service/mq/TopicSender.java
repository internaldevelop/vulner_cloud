package com.vulner.aqmp_bus.service.mq;

import com.vulner.aqmp_bus.global.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
