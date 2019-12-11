package com.vulner.system_log.controller;

import com.vulner.common.response.ResponseHelper;
import com.vulner.system_log.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

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

        systemLogService.writeLogFile(caller, account_uuid, type, title, contents, extra_info);

        return systemLogService.addLog(caller, account_uuid, type, title, contents, extra_info);
    }

    @GetMapping(value = "/get")
    @ResponseBody
    public Object getLogs(@RequestParam("caller")String caller,
                          @RequestParam("title")String title,
                          @RequestParam("offset")int offset,
                          @RequestParam("count")int count) {
        if (caller.length() == 0 || title.length() == 0) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        return systemLogService.getLogs(caller, title, offset, count);
    }

    @GetMapping(value = "/get_in_period")
    @ResponseBody
    public Object getLogs(@RequestParam("caller")String caller,
                          @RequestParam("title")String title,
                          @RequestParam("begin_time")Timestamp beginTime,
                          @RequestParam("end_time")Timestamp endTime,
                          @RequestParam("offset")int offset,
                          @RequestParam("count")int count) {
        if (caller.length() == 0 || title.length() == 0) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        return systemLogService.getLogs(caller, title, beginTime, endTime, offset, count);
    }
}