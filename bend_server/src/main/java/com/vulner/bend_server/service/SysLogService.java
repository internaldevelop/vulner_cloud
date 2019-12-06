package com.vulner.bend_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@FeignClient(value = "system-log")
public interface SysLogService {
    @GetMapping(value = "/sys_log/run_status")
    String runStatus();

    @PostMapping(value = "/sys_log/add")
    String addLog(@RequestParam("caller")String caller,
                  @RequestParam("account_uuid")String account_uuid,
                  @RequestParam("type")int type,
                  @RequestParam("title")String title,
                  @RequestParam("contents")String contents,
                  @RequestParam("extra_info")String extra_info);
}
