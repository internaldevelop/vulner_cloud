package com.vulner.bend_server.controller;

import com.vulner.bend_server.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticateApi {

    @Autowired
    AuthenticateService authenticateService;

    /**
     * 获取设备指纹生成对称秘钥 sym_key
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/get-fingerprint")
    @ResponseBody
    public Object getFingerprintToSymKey(@RequestParam("asset_uuid") String assetUuid) {
        return authenticateService.getFingerprintToSymKey(assetUuid);
    }

    /**
     * 获取公钥
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/get-public-key")
    @ResponseBody
    public Object getPublicKey(@RequestParam("asset_uuid") String assetUuid) {
        return authenticateService.getPublicKey(assetUuid);
    }

    /**
     * 认证
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/authenticate")
    @ResponseBody
    public Object authenticate(@RequestParam("asset_uuid") String assetUuid) {
        return authenticateService.authenticate(assetUuid);
    }

}
