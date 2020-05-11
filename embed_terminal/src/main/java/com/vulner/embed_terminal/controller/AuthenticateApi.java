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
     * 审核
     * @param assetUuid
     * @param classify
     * @return
     */
    @GetMapping(value = "/to-review")
    @ResponseBody
    public Object toReview(@RequestParam("asset_uuid") String assetUuid, @RequestParam("classify") int classify) {
        return authenticateService.toReview(assetUuid, classify);
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
     * 认证记录
     * @param assetUuid
     * @return
     */
    @GetMapping(value = "/authenticate-record")
    @ResponseBody
    public Object authenticateRecord(@RequestParam(value = "page_num", required = false)Integer pageNum,
                                     @RequestParam(value = "page_size", required = false)Integer pageSize,
                                     @RequestParam(required = false, value = "asset_uuid") String assetUuid) {
        return authenticateService.authenticateRecord(pageNum, pageSize, assetUuid);
    }

    /**
     * 认证记录详情
     * @param authUuid
     * @return
     */
    @GetMapping(value = "/authenticate-record-info")
    @ResponseBody
    public Object authenticateRecordInfo(@RequestParam(required = false, value = "auth_uuid") String authUuid) {
        return authenticateService.authenticateRecordInfo(authUuid);
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

    @GetMapping(value = "/again-generate-data")
    @ResponseBody
    public Object againGenerateData(@RequestParam("asset_uuid") String assetUuid) {
        return authenticateService.againGenerateData(assetUuid);
    }

}
