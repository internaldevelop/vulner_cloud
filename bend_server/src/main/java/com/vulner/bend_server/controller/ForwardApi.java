package com.vulner.bend_server.controller;

import com.vulner.bend_server.service.FirmwareAnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ForwardApi {

    @Autowired
    FirmwareAnalyzeService faService;

    @RequestMapping(value = "/firmware/{req_url}")
//    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object forwardFirmwareRequest(@PathVariable String req_url, @RequestParam String access_token, @RequestParam String params) {
        return faService.forwardRequest(req_url, access_token, params);
    }


}
