package com.vulner.aqmp_bus.global;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MAIN_EXCHANGE = "main-mq-exchange";

    public static final String DEFAULT_TOPIC = "topic.main";

    public static final String MAIN_FANOUT_EXCHANGE = "main-fanout-exchange";

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配
     */
    @Bean
    public TopicExchange defaultExchange() {
        return new TopicExchange(MAIN_EXCHANGE);
    }

    /**
     * 创建缺省主题 (topic)
     * 各节点向此主题发送消息，消费者是中心服务器
     * @return
     */
    @Bean
    public Queue mainQueue() {
        return new Queue(DEFAULT_TOPIC, true); //队列持久
    }

    /**
     * 绑定缺省主题到交换机
     * @return
     */
    @Bean
    public Binding bindDefaultTopic() {
        return BindingBuilder.bind(mainQueue()).to(defaultExchange()).with(RabbitConfig.DEFAULT_TOPIC);
    }

    /**
     * 广播交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(MAIN_FANOUT_EXCHANGE);
    }

    /**
     * 绑定节点的队列主题到交换机
     * @param topicName
     * @param routingKey
     * @return
     */
    public static Binding bindTopic(String topicName, String routingKey) {
        return BindingBuilder.bind(new Queue(topicName, true))
                .to(new TopicExchange(MAIN_EXCHANGE))
                .with(routingKey);
    }

//    @Bean
//    public RabbitAdmin rabbitAdmin(){
//        return new RabbitAdmin(connectionFactory());
//    }

}
