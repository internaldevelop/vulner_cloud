package com.vulner.firmware_fetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FirmwareFetchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirmwareFetchApplication.class, args);
    }

}
