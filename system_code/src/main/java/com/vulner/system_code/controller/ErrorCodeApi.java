package com.vulner.system_code.controller;

import com.vulner.system_code.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sys_code/err_codes")
public class ErrorCodeApi {
    @Autowired
    ErrorCodeService errorCodeService;

    @GetMapping(value = "/all")
    @ResponseBody
    public Object allErrorList() {
        return errorCodeService.getAllErrorCodes();
    }

    @GetMapping(value = "/by_code")
    @ResponseBody
    public Object errorByCode(@RequestParam("code") String code) {
        return errorCodeService.errorByCode(code);
    }

    @GetMapping(value = "/by_id")
    @ResponseBody
    public Object errorById(@RequestParam("id") int id) {
        return errorCodeService.errorById(id);
    }

    // "/sys_code/err_codes/all" 接口每次调用时都是重新获取错误码，
    // 因此，"/sys_code/err_codes/refresh"目前暂时没有用处。
    @GetMapping(value = "/refresh")
    @ResponseBody
    public Object regfresh() {
        return errorCodeService.refreshErrorCodes();
    }
}
