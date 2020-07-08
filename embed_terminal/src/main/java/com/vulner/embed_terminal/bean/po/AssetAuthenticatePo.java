package com.vulner.embed_terminal.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AssetAuthenticatePo {
    private int id;
    private String uuid;
    private String asset_uuid;
    private int authenticate_flag;  // 认证标识 默认值0 (1:验证通过; 2:验签错误; 3:解密错误; 4:授信过期)
    private String sym_key;  // 对称秘钥
    private String public_key;  // 公钥
    private String dev_fingerprint;  // 指纹
    private String plaintext;  // 明文
    private String ciphertext;  // 密文
    private String signature;  // 签名
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp update_time;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;
}
