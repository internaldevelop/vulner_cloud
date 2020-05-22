package com.vulner.embed_terminal.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import lombok.Data;

@Data
public class AssetAuthenticateDto extends AssetsPo {

    private String auth_uuid;
    private int authenticate_flag;
    private String sym_key;
    private String public_key;
    private String dev_fingerprint;
    private String plaintext;  // 明文
    private String ciphertext;  // 密文
    private String signature;  // 签名

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp auth_time;  // 认证时间
}
