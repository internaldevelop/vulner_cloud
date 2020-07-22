package com.vulner.system_log.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SystemLogBackupPo {
    private int id;
    private String uuid;
    private String user_uuid;  // 操作用户uuid
    private String user_name;  // 操作用户name
    private String file_url;  // 日志文件路径
    private String file_name;  // 日志文件名字
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp begin_time;  // 日志记录备份开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp end_time;  // 日志记录备份结束日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;
}
