package com.vulner.unify_auth.controller;

import com.vulner.unify_auth.bean.dto.AccountRegisterDto;
import com.vulner.unify_auth.service.AccountsManageService;
import com.vulner.unify_auth.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SystemApi {
    @Value("${server.port}")
    String port;
    @Value("${spring.application.name}")
    String appName;
    @Value("${myprops.defaultZone}")
    String defaultZone;

    @Autowired
    private AccountsManageService accountsManageService;

    @RequestMapping(value = "/actuator/info", method = RequestMethod.GET)
    @ResponseBody
    public Object actuatorInfo() {
        String info = appName + " is running on port: " + port + ". Register server is: " + defaultZone;
        return ResponseHelper.success(info);
    }

    @RequestMapping(value = "/actuator/info_test", method = RequestMethod.GET)
    @ResponseBody
    public Object actuatorInfoTest() {
        return actuatorInfo();
    }

    @RequestMapping(value = "/system/run_status", method = RequestMethod.GET)
    @ResponseBody
    public Object runStatus() {
        return actuatorInfo();
    }

    @PostMapping(value = "/system/account/register", produces = "application/json")
    public @ResponseBody
    Object addAccount(@Valid AccountRegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.invalidParams(bindingResult);
        }
        return accountsManageService.registerAccount(registerDto);
    }
}
