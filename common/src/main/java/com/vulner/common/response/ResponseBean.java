package com.vulner.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ResponseBean {
    private String code;
    private int id;
    private String error;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp timeStamp;
    private Object payload;
}
