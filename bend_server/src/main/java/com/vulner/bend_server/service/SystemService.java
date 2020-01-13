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
