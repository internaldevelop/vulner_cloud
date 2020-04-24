package com.vulner.embed_terminal.bean.dto;

import com.vulner.embed_terminal.bean.po.AssetsPo;
import lombok.Data;

@Data
public class AssetAuthenticateDto extends AssetsPo {

    private String auth_uuid;
    private int authenticate_flag;
    private String sym_key;
    private String public_key;
    private String dev_fingerprint;
}
