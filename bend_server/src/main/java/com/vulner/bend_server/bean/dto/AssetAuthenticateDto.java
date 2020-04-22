package com.vulner.bend_server.bean.dto;

import com.vulner.bend_server.bean.po.AssetsPo;
import lombok.Data;

@Data
public class AssetAuthenticateDto extends AssetsPo {

    private String auth_uuid;
    private int authenticate_flag;
    private String sym_key;
    private String public_key;
    private String dev_fingerprint;
}
