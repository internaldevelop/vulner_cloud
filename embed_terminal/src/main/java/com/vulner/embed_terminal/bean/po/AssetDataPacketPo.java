package com.vulner.embed_terminal.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AssetDataPacketPo {
    private int id;
    private String uuid;
    private String asset_uuid;
    private String transport_protocol;  // 传输协议tcp,udp,ICMP,arp
    private String app_protocol;  // 应用协议
    private String direction;  // 方向 1:上行; 2:下行
    private String source_ip;  // 源IP
    private String source_port;  // 源端口
    private String dest_ip;  // 目的IP
    private String dest_port;  // 目的端口
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp parse_time;  // 协议请求时间
    private String src_data;  // 原数据
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;

}
