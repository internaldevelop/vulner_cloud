package com.vulner.bend_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.client.RestTemplate;

//@SpringBootApplication(scanBasePackages = {"com.vulner.bend_server.controller", "com.vulner.bend_server.service", },
//        exclude = DataSourceAutoConfiguration.class)
//@SpringBootApplication(scanBasePackages = {"com.vulner.bend_server.controller", "com.vulner.bend_server.service",
//    "com.vulner.bend_server.global", "com.vulner.bend_server.configuration"})
@SpringBootApplication
@MapperScan(basePackages = {"com.vulner.bend_server.dao"})
//@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BendServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BendServerApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
