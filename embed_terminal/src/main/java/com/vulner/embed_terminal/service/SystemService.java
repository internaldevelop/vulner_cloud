package com.vulner.embed_terminal.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vulner.common.utils.TimeUtils;
import com.vulner.embed_terminal.bean.po.AssetDataPacketPo;
import com.vulner.embed_terminal.bean.po.AssetPerfPo;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import com.vulner.embed_terminal.bean.po.NetworkPo;
import com.vulner.embed_terminal.dao.*;
import com.vulner.embed_terminal.global.Page;
import com.vulner.embed_terminal.global.websocket.SockMsgTypeEnum;
import com.vulner.embed_terminal.global.websocket.WebSocketServer;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import lombok.Data;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.*;
import org.pcap4j.util.MacAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.Inet4Address;
import java.sql.Timestamp;
import java.util.*;
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

    @Autowired
    AssetDataPacketMapper assetDataPacketMapper;

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
                if (ip.indexOf(assetIp) > -1) {
                    assetPerfPo.setPackets_recv("" + jsonNet.get("packetsRecv"));
                    assetPerfPo.setBytes_recv("" + jsonNet.get("bytesRecv"));
                    assetPerfPo.setPackets_sent("" + jsonNet.get("packetsSent"));
                    assetPerfPo.setBytes_sent("" + jsonNet.get("bytesSent"));
                    jsonMsg.put("NewworkObj", jsonNet);
                    break;
                }

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

    public Object setPacketData(String assetUuid, String datas) {
        if (!StringUtils.isValid(assetUuid)) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }
        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp())) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        String assetIp = assetsPo.getIp();

        JSONObject jsonObject = JSONObject.parseObject(datas);
        String sourceIp = "" + jsonObject.get("source_ip");

        AssetDataPacketPo assetDataPacketPo = new AssetDataPacketPo();
        assetDataPacketPo.setUuid(StringUtils.generateUuid());
        assetDataPacketPo.setAsset_uuid(assetUuid);
        assetDataPacketPo.setCreate_time(TimeUtils.getCurrentSystemTimestamp());

        assetDataPacketPo.setSrc_data("" + jsonObject.get("src_data"));
        assetDataPacketPo.setSource_ip("" + jsonObject.get("source_ip"));
        assetDataPacketPo.setSource_port("" + jsonObject.get("source_port"));
        assetDataPacketPo.setDest_ip("" + jsonObject.get("dest_ip"));
        assetDataPacketPo.setDest_port("" + jsonObject.get("dest_port"));
        assetDataPacketPo.setTransport_protocol("" + jsonObject.get("transport_protocol"));
        assetDataPacketPo.setDirection((sourceIp.indexOf(assetIp) > -1) ? "1":"2");  // 方向 1:上行; 2:下行
        assetDataPacketPo.setApp_protocol("" + jsonObject.get("app_protocol"));

        assetDataPacketMapper.addAssetDataPacketData(assetDataPacketPo);

        return ResponseHelper.success();
    }

    /**
     * 获取数据包数据
     * @param assetUuid
     * @return
     */
    public Object getPacketDatas(Integer pageNum, Integer pageSize, String assetUuid, String transportProtocol, String startTime, String endTime) {

        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isValid(assetUuid))
            params.put("asset_uuid", assetUuid);
        if (StringUtils.isValid(transportProtocol))
            params.put("transport_protocol", transportProtocol);
        if (StringUtils.isValid(startTime))
            params.put("start_time", startTime);
        if (StringUtils.isValid(endTime))
            params.put("end_time", endTime);

        int totalCount = assetDataPacketMapper.getPacketDataCount(params);

        Page<AssetDataPacketPo> page = null;
        if (pageNum == null || pageSize == null) {
            pageSize = (pageSize == null) ? 10 : totalCount;
            page = new Page<>(1, pageSize);
        } else {
            params.put("start", Page.getStartPosition(pageNum, pageSize));
            params.put("count", pageSize);
            page = new Page<>(pageNum, pageSize);
        }

        List<AssetDataPacketPo> assetdatapacketlist = assetDataPacketMapper.getPacketDataList(params);
        page.setData(assetdatapacketlist);
        page.setTotalResults(totalCount);

        return ResponseHelper.success(page);
    }

    /**
     * 流量包统计
     * @param startTime
     * @param endTime
     * @return
     */
    public Object getPacketStatistics(String startTime, String endTime) {

        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isValid(startTime))
            params.put("start_time", startTime);
        if (StringUtils.isValid(endTime))
            params.put("end_time", endTime);

        Map<String, Object> retMap = new HashMap<>();

        int allUpCount = 0, alldownCount = 0;
        List<Map<String, Object>> packetStatistics = assetDataPacketMapper.getPacketStatistics(params);
        for (Map<String, Object> mp : packetStatistics) {
            allUpCount += Integer.parseInt(mp.get("sent_count").toString());
            alldownCount += Integer.parseInt(mp.get("recv_count").toString());
        }
        retMap.put("all_sent_count", allUpCount);
        retMap.put("all_recv_count", alldownCount);
        retMap.put("details", packetStatistics);

        return ResponseHelper.success(retMap);
    }

    /**
     * 启动数据包抓取
     * @param assetUuid
     * @return
     */
    public Object getPacketStart(String assetUuid) {
        if (!StringUtils.isValid(assetUuid))
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");

        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp()))
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");

        // 构造URL
        String url = "http://" + assetsPo.getIp() + ":8191/asset-network-info/start-get-packet?asset_uuid={asset_uuid}";

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
     * 停止数据包抓取
     * @param assetUuid
     * @return
     */
    public Object getPacketStop(String assetUuid) {
        if (!StringUtils.isValid(assetUuid))
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");

        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp()))
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");

        // 构造URL
        String url = "http://" + assetsPo.getIp() + ":8191/asset-network-info/stop-get-packet?asset_uuid={asset_uuid}";

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

    private static final String PCAP_FILE_KEY = SystemService.class.getName() + ".pcapFile";
    private static final String PCAP_FILE = System.getProperty(PCAP_FILE_KEY, "./pcap/modbus.pcap");
    private static final String IEC_PCAP_FILE = System.getProperty(PCAP_FILE_KEY, "./pcap/iec104.pcap");
    private static final String S7_PCAP_FILE = System.getProperty(PCAP_FILE_KEY, "./pcap/S7.pcap");

    public Object readPacket() {
        try {
            PcapHandle handle = Pcaps.openOffline(PCAP_FILE);
            PacketThread packetThread1 = new PacketThread();
            packetThread1.setHandle(handle);
            packetThread1.start();

            PcapHandle handleIec = Pcaps.openOffline(IEC_PCAP_FILE);
            PacketThread packetThread2 = new PacketThread();
            packetThread2.setHandle(handleIec);
            packetThread2.start();

            PcapHandle handleS7 = Pcaps.openOffline(S7_PCAP_FILE);
            PacketThread packetThread3 = new PacketThread();
            packetThread3.setHandle(handleS7);
            packetThread3.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseHelper.success();
    }

    @Data
    class PacketThread extends Thread {
        private PcapHandle handle;

        @Override
        public void run() {
            try {
                for (int i = 0; i < 50; i++) {

                    Packet packet = handle.getNextPacketEx();
                    if(packet == null) {
                        break;
                    }
                    gotPacket(packet);
                }
                handle.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String shortToStr(Short st){
        return "" + Short.toUnsignedInt(st);
    }
    public void gotPacket(org.pcap4j.packet.Packet packet) {

        JSONObject objRet = new JSONObject();

        List<AssetsPo> assetList = assetsMapper.getAssets();

        String sourceIp = "";  // 源IP
        String sourcePort = "";  // 源端口
        String destIp = "";  // 目的IP
        String destPort = "";  // 目的端口
        String transportProtocol = "";  // 传输协议
        String appProtocol = "";  // 应用协议

        org.pcap4j.packet.IpV4Packet ipV4Packet = packet.get(org.pcap4j.packet.IpV4Packet.class); // 直接获取IpV4报文
        if (ipV4Packet != null) {
            IpV4Packet.IpV4Header header = ipV4Packet.getHeader();

            Inet4Address srcAddr = header.getSrcAddr();
            Inet4Address dstAddr = header.getDstAddr();
            sourceIp = "" + srcAddr;
            destIp = "" + dstAddr;
            transportProtocol = header.getProtocol().name();

            // 可以直接get你想要的报文类型，只要Pcap4J库原生支持
            org.pcap4j.packet.EthernetPacket ethernetPacket = packet.get(org.pcap4j.packet.EthernetPacket.class); // 以太网报文

            if ("TCP".equals(transportProtocol)) {
                org.pcap4j.packet.TcpPacket tcpPacket = packet.get(org.pcap4j.packet.TcpPacket.class); // TCP报文

                TcpPacket.TcpHeader tcpHeader = tcpPacket.getHeader();
                sourcePort = shortToStr(tcpHeader.getSrcPort().value());  // 源端口
                destPort = shortToStr(tcpHeader.getDstPort().value());  // 目的端口

                org.pcap4j.packet.Packet tcpPayload = tcpPacket.getPayload();
                if (tcpPayload != null) {
                    byte[] data = tcpPayload.getRawData();
                    if (data != null && data.length > 1) {
                        byte datum = data[0];
                        byte datumLen = data[1];

                        if (datum == 0x68 && datumLen >= 0x04 && datumLen <= 0xFD){
                            appProtocol = "IEC104";  // 应用协议
                        } else if ("502".equals(sourcePort) || "502".equals(destPort)) {
                            appProtocol = "Modbus";  // 应用协议
                        } else if ("102".equals(sourcePort) || "102".equals(destPort)) {
                            appProtocol = "S7";  // 应用协议
                        }
                    }

                    int size = assetList.size();
                    Random ran = new Random(size);

                    for (int i = 0; i < size; i++) {
                        AssetsPo asset = assetList.get(ran.nextInt(10));

                        AssetDataPacketPo assetDataPacketPo = new AssetDataPacketPo();
                        assetDataPacketPo.setUuid(StringUtils.generateUuid());
                        assetDataPacketPo.setAsset_uuid(asset.getUuid());
                        assetDataPacketPo.setCreate_time(TimeUtils.getCurrentSystemTimestamp());

                        assetDataPacketPo.setSrc_data("" + packet);
                        assetDataPacketPo.setSource_ip(sourceIp.replace("/", ""));
                        assetDataPacketPo.setSource_port(sourcePort);
                        assetDataPacketPo.setDest_ip(destIp.replace("/", ""));
                        assetDataPacketPo.setDest_port(destPort);
                        assetDataPacketPo.setTransport_protocol(transportProtocol);
                        String direction = "2";  // 方向 1:上行; 2:下行
                        if ((sourceIp.indexOf("10.20.100.108") > -1) || (sourceIp.indexOf("192.168.25.146") > -1) || (sourceIp.indexOf("192.168.66.235") > -1)) {
                            direction = "1";
                        }
                        assetDataPacketPo.setDirection(direction);  // 方向 1:上行; 2:下行
                        assetDataPacketPo.setApp_protocol(appProtocol);

                        assetDataPacketMapper.addAssetDataPacketData(assetDataPacketPo);
                    }

                }
            }
        }
    }

}
