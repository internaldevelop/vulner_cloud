package com.vulner.system_code.service;

import com.vulner.common.bean.dto.ErrorCodeDto;
import com.vulner.common.response.ResponseHelper;
import com.vulner.system_code.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class ErrorCodeInitializer implements CommandLineRunner {
    @Autowired
    ErrorCodeService errorCodeService;

    @Override
    public void run(String... args) throws Exception {
        List<ErrorCodeDto> errorCodeDtos = errorCodeService.readAll();
        ResponseHelper.setErrorCodeList(errorCodeDtos);
    }
}
