package com.vulner.unify_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UnifyAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnifyAuthApplication.class, args);
    }

}
