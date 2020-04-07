package com.vulner.bend_server.controller;

import com.vulner.bend_server.service.FirmwareAnalyzeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ForwardApi {

    @Resource(name = "firmwareAnalyzeService")
    FirmwareAnalyzeService faService;

    @RequestMapping(value = "/firmware/{req_url}")
    @ResponseBody
    public Object forwardFirmwareRequest(HttpServletRequest request, HttpServletResponse response, @PathVariable String req_url) {
        return faService.forwardRequest(request, req_url);
    }


}
