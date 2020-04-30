package com.vulner.embed_terminal.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class NetworkPo {
    private int id;
    private String uuid;
    private String name;  // 网卡名称
    private String mac_address;  // MAC
    private String mtu;  // 最大传输单元
    private String speed;  // 速度
    private String ipv4;
    private String ipv6;
    private String bytes_recv;  // 接收字节数
    private String bytes_sent;  // 发送字节数
    private String packets_recv;  // 接收数据包
    private String packets_sent;  // 发送数据包
    private String asset_uuid;  // 资产uuid
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;

}
