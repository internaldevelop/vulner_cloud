package com.vulner.embed_terminal.controller;

import com.vulner.embed_terminal.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "network", produces = MediaType.APPLICATION_JSON_VALUE)
public class NetworkApi {
    @Autowired
    SystemService systemService;

    /**
     * 启动获取NetWork数据
     * @param assetUuid
     * @param types
     * @param secondTime
     * @return
     */
    @GetMapping(value = "/starttask")
    @ResponseBody
    public Object startNetWorkTask(@RequestParam("asset_uuid") String assetUuid, @RequestParam("types") String types, @RequestParam( value = "second_time", required = false) String secondTime) {
        return systemService.startTaskNetwork(assetUuid, types, secondTime);
    }

    /**
     * 停止获取NetWork数据
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/stoptask")
    @ResponseBody
    public Object stopNetWorkTask(@RequestParam("asset_uuid") String assetUuid) {
        return systemService.stopNetWorkTask(assetUuid);
    }

    /**
     * 保存数据
     * @param assetUuid
     * @param datas
     * @return
     */
    @GetMapping(value = "/setdata")
    @ResponseBody
    public Object setNetworkData(@RequestParam("asset_uuid") String assetUuid, @RequestParam("datas") String datas) {
        return systemService.setNetworkData(assetUuid, datas);
    }

}
