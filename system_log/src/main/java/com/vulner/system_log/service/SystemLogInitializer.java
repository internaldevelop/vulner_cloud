package com.vulner.system_log.service;

import com.vulner.common.bean.dto.ErrorCodeDto;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class SystemLogInitializer implements CommandLineRunner {
    @Autowired
    ErrorCodeService errorCodeService;

    @Override
    public void run(String... args) throws Exception {
        ResponseBean response = errorCodeService.allCodes();
        if (ResponseHelper.isSuccess(response)) {
            List<ErrorCodeDto> errorCodeDtos = (List<ErrorCodeDto>)response.getPayload();
            ResponseHelper.setErrorCodeList(errorCodeDtos);
        }
    }
}
