package com.vulner.embed_terminal.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import com.vulner.embed_terminal.bean.po.NetworkPo;
import com.vulner.embed_terminal.dao.AssetsMapper;
import com.vulner.embed_terminal.dao.NetworkMapper;
import com.vulner.embed_terminal.global.websocket.SockMsgTypeEnum;
import com.vulner.embed_terminal.global.websocket.WebSocketServer;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SystemService {
    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    AssetsMapper assetsMapper;

    @Autowired
    NetworkMapper networkMapper;

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

    /**
     * 系统资源信息放入Websocket
     * @param assetUuid
     * @param datas
     * @return
     */
    public Object setResourcesData(String assetUuid, String datas) {
        if (!StringUtils.isValid(assetUuid) || !StringUtils.isValid(datas))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");
        JSONObject jsonMsg = JSONObject.parseObject(datas);
        jsonMsg.put("asset_uuid", assetUuid);
        WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.ASSET_REAL_TIME_INFO, jsonMsg);
        return ResponseHelper.success();
    }

    /**
     * 启动任务获取系统资源
     * @param assetUuid
     * @param types
     * @param secondTime
     * @return
     */
    public Object startTaskResources(String assetUuid, String types, String secondTime) {

        if (!StringUtils.isValid(assetUuid))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp()))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        // 构造URL
        String url = "http://" + assetsPo.getIp() + ":8191/asset-info/start-task-acquire?asset_uuid={asset_uuid}&types={types}&second_time={second_time}";

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

    /**
     * 保存流量数据
     * @param assetUuid
     * @param datas
     * @return
     */
    public Object setNetworkData(String assetUuid, String datas) {
        if (!StringUtils.isValid(assetUuid) || !StringUtils.isValid(datas))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        JSONArray jsonArray = JSONArray.parseArray(datas);
        for(Object obj : jsonArray) {
            JSONObject jsonMsg = (JSONObject)JSONObject.toJSON(obj);
            NetworkPo networkPo = new NetworkPo();
            networkPo.setUuid(StringUtils.generateUuid());
            networkPo.setAsset_uuid(assetUuid);
            networkPo.setName("" + jsonMsg.get("netWorkName"));
            networkPo.setMac_address("" + jsonMsg.get("macAddress"));
            networkPo.setIpv4("" + jsonMsg.get("IPv4"));
            networkPo.setIpv6("" + jsonMsg.get("IPv6"));
            networkPo.setPackets_recv("" + jsonMsg.get("packetsRecv"));
            networkPo.setBytes_recv("" + jsonMsg.get("bytesRecv"));
            networkPo.setPackets_sent("" + jsonMsg.get("packetsSent"));
            networkPo.setSpeed("" + jsonMsg.get("speed"));
            networkPo.setMtu("" + jsonMsg.get("mtu"));

            long lt = new Long("" + jsonMsg.get("timeStamp"));
            networkPo.setCreate_time(new Timestamp(lt));

            networkMapper.addNetwork(networkPo);
        }
        Map<String, Object> mpMsg = new HashMap<>();
        mpMsg.put("asset_uuid", assetUuid);
        mpMsg.put("net_datas", jsonArray);
        WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.ASSET_NETWORK_INFO, mpMsg);

        return ResponseHelper.success();
    }

    /**
     * 启动获取流量数据
     * @param assetUuid
     * @param types
     * @param secondTime
     * @return
     */
    public Object startTaskNetwork(String assetUuid, String types, String secondTime) {

        if (!StringUtils.isValid(assetUuid))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp()))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        // 构造URL
        String url = "http://" + assetsPo.getIp() + ":8191/asset-network-info/get-network?asset_uuid={asset_uuid}&types={types}&second_time={second_time}";

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

    /**
     * 停止获取NetWork数据
     * @param assetUuid
     * @return
     */
    public Object stopNetWorkTask(String assetUuid) {

        if (!StringUtils.isValid(assetUuid))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp()))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        // 构造URL
        String url = "http://" + assetsPo.getIp() + ":8191/asset-network-info/stop-get-network?asset_uuid={asset_uuid}";

        // 构造参数map
        HashMap<String, String> map = new HashMap<>();
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
