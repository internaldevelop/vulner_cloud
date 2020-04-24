package com.vulner.embed_terminal.controller;

import com.vulner.embed_terminal.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticateApi {

    @Autowired
    AuthenticateService authenticateService;

    /**
     * 根据ip段扫描设备
     * @param startIp
     * @param endIp
     * @return
     */
    @GetMapping(value = "/scan-get-equipment")
    @ResponseBody
    public Object scanGetEquipment(@RequestParam("start_ip") String startIp, @RequestParam("end_ip") String endIp) {
        return authenticateService.scanGetEquipment(startIp, endIp);
    }

    /**
     * 授权
     * @param assetUuid
     * @param empowerFlag
     * @return
     */
    @GetMapping(value = "/to-authorizate")
    @ResponseBody
    public Object toAuthorizate(@RequestParam("asset_uuid") String assetUuid, @RequestParam("empower_flag") int empowerFlag) {
        return authenticateService.toAuthorizate(assetUuid, empowerFlag);
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

}
