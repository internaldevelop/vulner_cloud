package com.vulner.embed_terminal.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AssetPerfPo {
    private int id;
    private String uuid;
    private String asset_uuid;
    private String cpu_free_percent;  // cpu空闲率
    private String cpu_used_percent;  // cpu使用率
    private String memory_free_percent;  // 内存空闲率
    private String memory_used_percent;  // 内存使用率
    private String disk_free_percent;  // 磁盘空闲率
    private String disk_used_percent;  // 磁盘使用率
    private String packets_recv;  // 网络
    private String packets_sent;
    private String bytes_recv;
    private String bytes_sent;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;

}
