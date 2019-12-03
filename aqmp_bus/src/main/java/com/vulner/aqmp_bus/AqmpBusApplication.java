package com.vulner.aqmp_bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AqmpBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(AqmpBusApplication.class, args);
    }

}
