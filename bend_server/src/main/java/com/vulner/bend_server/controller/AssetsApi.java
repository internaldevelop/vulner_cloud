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
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/get-assets")
    @ResponseBody
    public Object getAssets(@RequestParam(value = "asset_uuid", required = false) String assetUuid) {
        return assetsService.getAssets(assetUuid);
    }

}
