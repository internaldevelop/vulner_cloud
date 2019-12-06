package com.vulner.unify_auth.service;

import com.vulner.common.response.ResponseBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "system-code")
public interface ErrorCodeService {
    @RequestMapping(value = "/sys_code/run_status", method = RequestMethod.GET)
    String runStatus();

    @GetMapping(value = "/sys_code/err_codes/all")
    ResponseBean allErrorCodes();
}
