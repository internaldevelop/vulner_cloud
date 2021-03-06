package com.vulner.embed_terminal.service;

import com.vulner.common.response.ResponseBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "system-code")
public interface ErrorCodeService {
    @GetMapping(value = "/sys_code/run_status")
    ResponseBean runStatus();

    @GetMapping(value = "/sys_code/err_codes/all")
    ResponseBean allErrorCodes();
}
