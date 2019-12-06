package com.vulner.system_log.dao;

import com.vulner.system_log.bean.po.SystemLogPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SystemLogsMapper {
    @Insert("INSERT INTO sys_logs( \n" +
            "uuid, caller, create_account_uuid, \n" +
            "type, title, contents, \n" +
            "extra_info, create_time) \n" +
            "VALUES ( \n" +
            "#{uuid}, #{caller}, #{create_account_uuid}, \n" +
            "#{type}, #{title}, #{contents}, \n" +
            "#{extra_info}, #{create_time, jdbcType=TIMESTAMP}) ")
    int addLog(SystemLogPo systemLogPo);

    @Select("SELECT \n" +
            "    s.id, \n" +
            "    s.uuid, \n" +
            "    s.type, \n" +
            "    s.caller, \n" +
            "    s.create_account_uuid, \n" +
            "    s.title, \n" +
            "    s.contents, \n" +
            "    s.extra_info, \n" +
            "    s.create_time \n" +
            "FROM sys_logs s \n")
    List<SystemLogPo> getAllLogs();
}
