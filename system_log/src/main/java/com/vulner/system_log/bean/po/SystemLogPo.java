package com.vulner.system_log.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SystemLogPo {
    private int id;
    private String uuid;
    // 调用者（其他模块或系统名称）
    private String caller;
    // 系统或模块的使用者账户 UUID
    private String account_info;
    //    日志记录的类型：
    //            1：成功操作；
    //            2：失败操作；
    //            3：系统错误（严重错误）；
    //            4：一般信息；
    //            5：异常信息（一般异常）；
    //            6：警告；
    private int type;
    // 日志标题
    private String title;
    // 日志内容
    private String contents;
    // 扩展信息
    private String extra_info;
    // 日志创建时间（日志服务端的时间）
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp create_time;
}
