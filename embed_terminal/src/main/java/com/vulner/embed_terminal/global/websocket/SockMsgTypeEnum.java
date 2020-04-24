package com.vulner.embed_terminal.global.websocket;

public enum SockMsgTypeEnum {
    GENERAL_INFO(0, "一般信息"),        // payload为字符串
    ASSET_REAL_TIME_INFO(1, "资产实时系统状态数据"), // payload为System + CPU实时数据 + Mem实时数据
    SCAN_ASSET_INFO(2, "扫描资产")
    ;

    private int type;
    private String message;

    SockMsgTypeEnum(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
