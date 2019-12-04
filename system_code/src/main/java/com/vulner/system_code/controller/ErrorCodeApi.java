package com.vulner.system_code.controller;

import com.vulner.common.response.ResponseHelper;
import com.vulner.system_code.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sys_code/err_codes")
public class ErrorCodeApi {
    @Autowired
    ErrorCodeService errorCodeService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Object allCodes() {

        return ResponseHelper.success(errorCodeService.readAll());
    }
}
