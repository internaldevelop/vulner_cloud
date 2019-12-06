package com.vulner.system_code.controller;

import com.vulner.common.response.ResponseHelper;
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

        return ResponseHelper.success(errorCodeService.readAll());
    }

    @GetMapping(value = "/by_code")
    @ResponseBody
    public Object errorByCode(@RequestParam("code") String code) {
        return ResponseHelper.success(ResponseHelper.getErrorByCode(code));
    }

    @GetMapping(value = "/by_id")
    @ResponseBody
    public Object errorById(@RequestParam("id") int id) {
        return ResponseHelper.success(ResponseHelper.getErrorById(id));
    }
}
