package com.vulner.embed_terminal.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AssetsPo {
    private int id;
    private String uuid;
    private int classify;  // 审核分类0: (1:通过(白名单); -1:拒绝(黑名单))
    private String code;  // 资产代号
    private String name;  // 资产名称
    private String ip;  // 资产的IP地址
    private String port;  // 资产的IP地址
    private String os_type;  // 操作系统的类型或系列(1:windows; 2:linux)
    private String os_ver;  // 操作系统的版本
    private String create_user_uuid;  // 创建资产的用户 UUID
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp expire_time;  // 到期时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp update_time;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;


}
