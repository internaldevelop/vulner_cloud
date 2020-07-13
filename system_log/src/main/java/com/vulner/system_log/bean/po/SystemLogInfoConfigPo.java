package com.vulner.system_log.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SystemLogInfoConfigPo {
    private int id;
    private String uuid;
    private String log_field;  // 日志字段
    private String log_field_desc;  // 日志字段描述
    private String is_display;  // 是否显示 0:不显示; 1:显示
    private String is_default;  // 显示状态是否可修改 0:不可修改; 1:可修改
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp update_time;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;

}
