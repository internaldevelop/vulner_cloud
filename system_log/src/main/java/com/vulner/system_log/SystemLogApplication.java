package com.vulner.system_log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SystemLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemLogApplication.class, args);
    }

}
