package com.vulner.embed_terminal.controller;

import com.vulner.embed_terminal.service.AssetsService;
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
     * @param classify
     * @param authenticateFlag
     * @param flag 1:不展示指纹; 2:展示指纹
     * @return
     */
    @GetMapping(value = "/get-assets")
    @ResponseBody
    public Object getAssets(@RequestParam(value = "page_num", required = false)Integer pageNum,
                            @RequestParam(value = "page_size", required = false)Integer pageSize,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "ip", required = false) String ip,
                            @RequestParam(value = "os_type", required = false)String osType,
                            @RequestParam(value = "classify", required = false) String classify,
                            @RequestParam(value = "authenticate_flag", required = false) String authenticateFlag,
                            @RequestParam(value = "flag", required = false) String flag) {
        return assetsService.getAssets(pageNum, pageSize, name, ip, osType, classify, authenticateFlag, flag);
    }

    /**
     * 获取设备详细信息
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/get-asset-info")
    @ResponseBody
    public Object getAssetInfo(@RequestParam(value = "asset_uuid") String assetUuid) {
        return assetsService.getAssetInfo(assetUuid);
    }

    /**
     * 获取设备资产 资源历史数据
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/get-his-resources")
    @ResponseBody
    public Object getHisResources(@RequestParam(value = "page_num", required = false)Integer pageNum,
                                  @RequestParam(value = "page_size", required = false)Integer pageSize,
                                  @RequestParam(value = "asset_uuid") String assetUuid) {
        return assetsService.getHisResources(pageNum, pageSize, assetUuid);
    }

    /**
     * 资产统计
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping(value = "/get-statistics")
    @ResponseBody
    public Object getStatistics(@RequestParam(required = false, value = "start_time") String startTime,
                                @RequestParam(required = false, value = "end_time") String endTime) {
        return assetsService.getStatistics(startTime, endTime);
    }



}
