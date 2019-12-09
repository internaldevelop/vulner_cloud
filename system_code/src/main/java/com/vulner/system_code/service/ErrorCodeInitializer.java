package com.vulner.system_code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ErrorCodeInitializer implements CommandLineRunner {
    @Autowired
    ErrorCodeService errorCodeService;

    @Override
    public void run(String... args) throws Exception {
        errorCodeService.refreshErrorCodes();
    }
}
