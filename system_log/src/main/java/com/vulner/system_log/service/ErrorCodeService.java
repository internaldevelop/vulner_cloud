package com.vulner.system_log.service;

import com.vulner.common.response.ResponseBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "system-code")
public interface ErrorCodeService {
    @GetMapping(value = "/sys_code/run_status")
    String runStatus();

    @GetMapping(value = "/sys_code/err_codes/all")
    ResponseBean allCodes();
}
