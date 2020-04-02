package com.vulner.bend_server.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@RestController
@FeignClient(value = "firmware-analyze")
public interface FirmwareAnalyzeService {

    @RequestMapping(value = "/{req_url}")
    Object forwardRequest(@PathVariable String req_url, @RequestParam("access_token") String access_token, @RequestParam("params") String params);
}
