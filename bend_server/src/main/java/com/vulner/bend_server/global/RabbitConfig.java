package com.vulner.bend_server.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MAIN_FANOUT_EXCHANGE = "main-fanout-exchange";

    /**
     * 广播交换机.
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(MAIN_FANOUT_EXCHANGE);
    }

}
