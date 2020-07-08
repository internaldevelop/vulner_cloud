package com.vulner.bend_server.service;

import com.alibaba.fastjson.JSONObject;
import com.vulner.bend_server.global.websocket.SockMsgTypeEnum;
import com.vulner.bend_server.global.websocket.WebSocketServer;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class SystemService {
    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    RestTemplate restTemplate;

    @Bean(name="remoteRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public Object getHostSystemInfo() {
        JSONObject sysInfo = new JSONObject();
        sysInfo.put("sysName", "电力工控固件漏洞挖掘工具");
        sysInfo.put("desc", "对电力工控固件深层次漏洞挖掘。");
        sysInfo.put("sysVer", "1.0.0.1001");
        sysInfo.put("copyright", "Copyright ©2019-2022 中国电科院");
        sysInfo.put("status", "运行中");
        sysInfo.put("overview", "建设对电力智能电网系统软硬件和通讯协议进行漏洞挖掘的技术手段，" +
                "根据攻击层次的不同，这些针对智能电网系统的恶意攻击可以划分为利用应用软件（包括智能电网软件）漏洞、" +
                "利用通信协议漏洞以及基于硬件漏洞三种类型。");

        return ResponseHelper.success(sysInfo);
    }

    public Object setResourcesData(String assetUuid, String datas) {
        JSONObject jsonMsg = JSONObject.parseObject(datas);
        jsonMsg.put("asset_uuid", assetUuid);
        WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.ASSET_REAL_TIME_INFO, jsonMsg);
        return ResponseHelper.success();
    }

    public Object setFirmwareData(String taskUuid, String datas) {
        JSONObject jsonMsg = JSONObject.parseObject(datas);
        jsonMsg.put("task_uuid", taskUuid);
        WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.FIRMWARE_INFO, jsonMsg);
        return ResponseHelper.success();
    }

    public Object startTaskResources(String assetUuid, String types, String secondTime) {

        String assetIp = "localhost";
        // 构造URL
        String url = "http://" + assetIp + ":8191/asset-info/start-task-acquire?types={types}&second_time={second_time}&asset_uuid={asset_uuid}";

        // 构造参数map
        HashMap<String, String> map = new HashMap<>();
        map.put("types", types);
        map.put("second_time", secondTime);
        map.put("asset_uuid", assetUuid);

        try {
            // 向节点发送请求，并返回节点的响应结果
            ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(url, Boolean.class, map);

            if (responseEntity.getBody()) {
                return ResponseHelper.success();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseHelper.error("ERROR_GENERAL_ERROR");
    }


}
