package com.vulner.embed_terminal.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import lombok.Data;

@Data
public class AssetAuthenticateRecordDto {

    private String auth_uuid;
    private String asset_uuid;
    private int authenticate_flag;  // 设备状态  1:验签成功; 2:验签失败; 3:解密成功; 4:解密失败
    private String sym_key;
    private String public_key;
    private String dev_fingerprint;
    private String plaintext;  // 明文
    private String ciphertext;  // 密文
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp auth_time;

    private AssetsPo asset;  // 设备信息
}
