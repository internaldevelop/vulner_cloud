package com.vulner.unify_auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableAuthorizationServer
@MapperScan(basePackages = {"com.vulner.unify_auth.dao"})
public class UnifyAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnifyAuthApplication.class, args);
    }

}
