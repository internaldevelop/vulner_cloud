package com.vulner.embed_terminal.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vulner.common.utils.TimeUtils;
import com.vulner.embed_terminal.bean.po.AssetPerfPo;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import com.vulner.embed_terminal.bean.po.NetworkPo;
import com.vulner.embed_terminal.dao.AssetPerfMapper;
import com.vulner.embed_terminal.dao.AssetsMapper;
import com.vulner.embed_terminal.dao.AuthenticateMapper;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class SystemService {
    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    AssetsMapper assetsMapper;

    @Autowired
    NetworkMapper networkMapper;

    @Autowired
    AuthenticateService authenticateService;

    @Autowired
    AssetPerfMapper assetPerfMapper;

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

        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp()))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        JSONObject jsonMsg = JSONObject.parseObject(datas);
        jsonMsg.put("asset_uuid", assetUuid);

        AssetPerfPo assetPerfPo = new AssetPerfPo();

        boolean addDataFlag = false;
        Object cpuObj = jsonMsg.get("CPU");
        if (cpuObj != null) {
            addCPUData(assetPerfPo, cpuObj);
            addDataFlag = true;
        }

        Object memoryObj = jsonMsg.get("Memory");
        if (memoryObj != null) {
            addMemoryData(assetPerfPo, memoryObj);
            addDataFlag = true;
        }
        Object disksObj = jsonMsg.get("Disks");
        if (disksObj != null) {
            addDisksData(assetPerfPo, disksObj);
            addDataFlag = true;
        }
        addNetworkData(assetPerfPo, jsonMsg, assetsPo.getIp());

        if (addDataFlag) {
            assetPerfPo.setUuid(StringUtils.generateUuid());
            assetPerfPo.setAsset_uuid(assetUuid);
            assetPerfPo.setCreate_time(TimeUtils.getCurrentSystemTimestamp());
            assetPerfMapper.addAssetPerfData(assetPerfPo);
        }

        WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.ASSET_REAL_TIME_INFO, jsonMsg);
        return ResponseHelper.success();
    }

    public void addCPUData (AssetPerfPo assetPerfPo, Object cpuObj) {
        JSONObject jsonMsg = (JSONObject)JSONObject.toJSON(cpuObj);
        assetPerfPo.setCpu_free_percent("" + jsonMsg.get("freePercentTotal"));
        assetPerfPo.setCpu_used_percent("" + jsonMsg.get("usedPercentTotal"));
    }

    public void addMemoryData (AssetPerfPo assetPerfPo, Object memoryObj) {
        JSONObject jsonMsg = (JSONObject)JSONObject.toJSON(memoryObj);
        assetPerfPo.setMemory_free_percent("" + jsonMsg.get("freePercentTotal"));
        assetPerfPo.setMemory_used_percent("" + jsonMsg.get("usedPercentTotal"));
    }

    public void addDisksData (AssetPerfPo assetPerfPo, Object disksObj) {
        JSONObject jsonMsg = (JSONObject)JSONObject.toJSON(disksObj);
        assetPerfPo.setDisk_free_percent("" + jsonMsg.get("freePercentTotal"));
        assetPerfPo.setDisk_used_percent("" + jsonMsg.get("usedPercentTotal"));
    }

    public void addNetworkData (AssetPerfPo assetPerfPo, JSONObject jsonMsg, String assetIp) {
        Object networkObj = jsonMsg.get("Network");
        if (networkObj != null) {
            JSONArray jsonArray = (JSONArray)networkObj;

            for(Object obj : jsonArray) {
                JSONObject jsonNet = (JSONObject)JSONObject.toJSON(obj);
                NetworkPo networkPo = new NetworkPo();
                setNetworkPo(networkPo, jsonNet);
                networkMapper.addNetwork(networkPo);

                String ip = "" + jsonNet.get("IPv4");
                if (ip.indexOf(assetIp) < 0)
                    continue;

                assetPerfPo.setPackets_recv("" + jsonNet.get("packetsRecv"));
                assetPerfPo.setBytes_recv("" + jsonNet.get("bytesRecv"));
                assetPerfPo.setPackets_sent("" + jsonNet.get("packetsSent"));
                assetPerfPo.setBytes_sent("" + jsonNet.get("bytesSent"));
                jsonMsg.put("NewworkObj", jsonNet);
            }
        }
    }

    public void setNetworkPo (NetworkPo networkPo, JSONObject jsonMsg) {
        networkPo.setUuid(StringUtils.generateUuid());
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
        String url = "http://" + assetsPo.getIp() + ":8191/asset-info/start-task-acquire?asset_uuid={asset_uuid}&types={types}&second_time={second_time}&detail={detail}";

        // 构造参数map
        HashMap<String, String> map = new HashMap<>();
        map.put("types", types);
        map.put("second_time", secondTime);
        map.put("asset_uuid", assetUuid);
        map.put("detail", "1");

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
     * 停止任务获取系统资源
     * @param assetUuid
     * @return
     */
    public Object stopTaskResources(String assetUuid) {

        if (!StringUtils.isValid(assetUuid))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp()))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        // 构造URL
        String url = "http://" + assetsPo.getIp() + ":8191/asset-info/stop-task-acquire?asset_uuid={asset_uuid}";

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
            JSONObject jsonNet = (JSONObject)JSONObject.toJSON(obj);
            NetworkPo networkPo = new NetworkPo();
            networkPo.setAsset_uuid(assetUuid);

            setNetworkPo(networkPo, jsonNet);
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

    /**
     * 启动定时任务
     * @param types
     * @param secondTime
     * @return
     */
    public Object startTaskResourcesAll(String types, String secondTime) {
        if (!StringUtils.isValid(types)) {
            types = "CPU,Memory,Disks,Network";
        }

        List<AssetsPo> assetList = assetsMapper.getAssets();

        if (assetList == null || assetList.size() < 1)
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");

        ExecutorService executor = Executors.newFixedThreadPool(1);
        String finalTypes = types;
        Future<Boolean> future = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call(){
                for (AssetsPo assetPo: assetList) {
                    String assetIp = assetPo.getIp();
                    String assetUuid = assetPo.getUuid();

                    if(authenticateService.verifyIp(assetIp)) {
                        // 构造URL
                        String url = "http://" + assetIp + ":8191/asset-info/start-task-acquire?asset_uuid={asset_uuid}&types={types}&second_time={second_time}&detail={detail}";

                        // 构造参数map
                        HashMap<String, String> map = new HashMap<>();
                        map.put("types", finalTypes);
                        map.put("second_time", secondTime);
                        map.put("asset_uuid", assetUuid);
                        map.put("detail","0");

                        try {
                            restTemplate.getForEntity(url, Boolean.class, map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });

        return ResponseHelper.success();
    }

    public Object stopTaskResourcesAll() {
        List<AssetsPo> assetList = assetsMapper.getAssets();

        if (assetList == null || assetList.size() < 1)
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Boolean> future = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call(){
                for (AssetsPo assetPo: assetList) {
                    String assetIp = assetPo.getIp();
                    String assetUuid = assetPo.getUuid();

                    if(authenticateService.verifyIp(assetIp)) {
                        // 构造URL
                        String url = "http://" + assetIp + ":8191/asset-info/stop-task-acquire?asset_uuid={asset_uuid}";
                        // 构造参数map
                        HashMap<String, String> map = new HashMap<>();
                        map.put("asset_uuid", assetUuid);

                        try {
                            restTemplate.getForEntity(url, Boolean.class, map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });

        return ResponseHelper.success();
    }
}
