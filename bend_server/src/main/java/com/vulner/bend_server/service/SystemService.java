package com.vulner.bend_server.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vulner.bend_server.global.SigarUtils;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.SystemUtils;
import org.hyperic.sigar.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SystemService {

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

    public Object echoAcquire(String types) {

        boolean bAll = false;
        List<String> typeList = null;
        if (types.isEmpty()) {
            // if types has not assigned, means all the info are needed.
            bAll = true;
        } else {
            typeList = Arrays.asList(types.split(","));
        }

        JSONObject jsonInfo = new JSONObject();

        // CPU
        if (bAll || typeList.contains("CPU")) {
            CpuInfo[] cpuInfos = SigarUtils.getCpuInfos();
            jsonInfo.put("CPU", cpuInfos);
        }

        // CPU percent
        if (bAll || typeList.contains("CPU Usage")) {
            CpuPerc[] cpuPercs = SigarUtils.getCpuPercent();
            jsonInfo.put("CPU percents", cpuPercs);
            jsonInfo.put("CPU usage", SigarUtils.getCpuTotalPercent(cpuPercs));
        }

        // System property
        if (bAll || typeList.contains("System")) {
            JSONObject props = SystemUtils.getProps();
            jsonInfo.put("System", props);
        }

        // Memory
        if (bAll || typeList.contains("Mem")) {
            Mem memory = SigarUtils.getMemInfos();
            jsonInfo.put("Memory", memory);
            Swap swap = SigarUtils.getSwapInfos();
            jsonInfo.put("Swap", swap);
        }

        // Who infos
        if (bAll || typeList.contains("Who")) {
            Who[] whos = SigarUtils.getWhoInfos();
            jsonInfo.put("Who", whos);
        }

        // File System
        if (bAll || typeList.contains("FS")) {
            JSONArray fsInfos = SigarUtils.getFSInfos();
            jsonInfo.put("FS", fsInfos);
        }

        // 总磁盘信息
        if (bAll || typeList.contains("FST")) {
            JSONObject fsTotalInfos = SigarUtils.getFSTotalInfo();
            jsonInfo.put("FST", fsTotalInfos);
        }

        // Network interfaces
        if (bAll || typeList.contains("Net Config")) {
            List<NetInterfaceConfig> configs = SigarUtils.getNetIConfig();
            jsonInfo.put("Net Config", configs);
        }

        // Domain infos
        if (bAll || typeList.contains("Domain")) {
            jsonInfo.put("Domain", SigarUtils.getDomainInfos());
        }

        // Network interfaces statics
        if (bAll || typeList.contains("Net Statics")) {
            JSONArray stat = SigarUtils.getIFStatInfos();
            jsonInfo.put("Net Statics", stat);
        }

        // Process CPU Usage: ratio / ranking
        if (bAll || typeList.contains("Proc CPU Ranking")) {
            JSONArray usage = SigarUtils.getCpuUsage();
            jsonInfo.put("Proc CPU Ranking", usage);
        }

        // Process Memory Usage: ratio / ranking
        if (bAll || typeList.contains("Proc Memory Ranking")) {
            JSONArray usage = SigarUtils.getMemoryUsage();
            jsonInfo.put("Proc Memory Ranking", usage);
        }

        return ResponseHelper.success(jsonInfo);
    }
}
