package com.vulner.system_log.controller;

import com.vulner.common.response.ResponseHelper;
import com.vulner.system_log.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sys_log")
public class LogApi {
    @Autowired
    SystemLogService systemLogService;

    @PostMapping(value = "/add")
    @ResponseBody
    public Object recordLog(@RequestParam("caller")String caller,
                            @RequestParam("account_uuid")String account_uuid,
                            @RequestParam("type")int type,
                            @RequestParam("title")String title,
                            @RequestParam("contents")String contents,
                            @RequestParam("extra_info")String extra_info) {
        boolean rv = systemLogService.addLog(caller, account_uuid, type, title, contents, extra_info);
        if (rv) {
            return ResponseHelper.success();
        } else {
            return ResponseHelper.error("ERROR_ADD_LOG_FAILED");
        }
    }
}
