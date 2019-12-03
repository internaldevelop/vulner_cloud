package com.vulner.system_code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SystemCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemCodeApplication.class, args);
    }

}
