package com.vulner.bend_server.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AssetAuthenticatePo {
    private int id;
    private String uuid;
    private String asset_uuid;
    private int authenticate_flag;  // 认证标识默认值为0未认证(-1:失败；1:成功)
    private String sym_key;  // 对称秘钥
    private String public_key;  // 公钥
    private String dev_fingerprint;  // 指纹
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp update_time;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;
}
