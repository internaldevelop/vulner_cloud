package com.vulner.bend_server.controller;

import com.vulner.bend_server.service.AssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "assets", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssetsApi {

    @Autowired
    AssetsService assetsService;

    /**
     * 获取设备列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param ip
     * @param osType
     * @param empowerFlag
     * @param authenticateFlag
     * @return
     */
    @GetMapping(value = "/get-assets")
    @ResponseBody
    public Object getAssets(@RequestParam(value = "page_num", required = false)Integer pageNum,
                            @RequestParam(value = "page_size", required = false)Integer pageSize,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "ip", required = false) String ip,
                            @RequestParam(value = "os_type", required = false)String osType,
                            @RequestParam(value = "empower_flag", required = false) String empowerFlag,
                            @RequestParam(value = "authenticate_flag", required = false) String authenticateFlag) {
        return assetsService.getAssets(pageNum, pageSize, name, ip, osType, empowerFlag, authenticateFlag);
    }

}
