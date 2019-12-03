package com.vulner.firmware_analyze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FirmwareAnalyzeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirmwareAnalyzeApplication.class, args);
    }

}
