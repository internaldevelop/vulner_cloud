package com.vulner.bend_server.service.logger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "system-log")
public interface SysLogService {
    @GetMapping(value = "/sys_log/run_status")
    String runStatus();

    @PostMapping(value = "/sys_log/add")
    String addLog(@RequestParam("access_token") String access_token,
                  @RequestParam("caller") String caller,
                  @RequestParam("account_info") String account_info,
                  @RequestParam("type") int type,
                  @RequestParam("title") String title,
                  @RequestParam("contents") String contents,
                  @RequestParam("extra_info") String extra_info);
}
