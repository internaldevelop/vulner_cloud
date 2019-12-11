package com.vulner.bend_server.service.mq;

import com.vulner.bend_server.global.RabbitConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FanoutReceiver {

    @Autowired
    RabbitConfig amqpConfig;

    public static final String BEND_SERVER_QUEUE = "bend_server_queue";

    @Bean
    public Queue bendServerQueue() {
        return new Queue(BEND_SERVER_QUEUE);
    }

    @Bean
    public Binding product2QueueBinding() {
        return BindingBuilder.bind(bendServerQueue()).to(amqpConfig.fanoutExchange());
    }

    @RabbitListener(queues = BEND_SERVER_QUEUE)
    public void product1(String data) {
        System.out.println(data+"----------" + BEND_SERVER_QUEUE);
    }

}
