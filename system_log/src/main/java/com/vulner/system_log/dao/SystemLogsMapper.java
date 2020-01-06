package com.vulner.system_log.dao;

import com.vulner.system_log.bean.po.SystemLogPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public interface SystemLogsMapper {
    @Insert("INSERT INTO sys_logs( \n" +
            "uuid, caller, account_info, \n" +
            "type, title, contents, \n" +
            "extra_info, create_time) \n" +
            "VALUES ( \n" +
            "#{uuid}, #{caller}, #{account_info}, \n" +
            "#{type}, #{title}, #{contents}, \n" +
            "#{extra_info}, #{create_time, jdbcType=TIMESTAMP}) ")
    int addLog(SystemLogPo systemLogPo);

    @Select("SELECT \n" +
            "    s.id, \n" +
            "    s.uuid, \n" +
            "    s.type, \n" +
            "    s.caller, \n" +
            "    s.account_info, \n" +
            "    s.title, \n" +
            "    s.contents, \n" +
            "    s.extra_info, \n" +
            "    s.create_time \n" +
            "FROM sys_logs s \n")
    List<SystemLogPo> getAllLogs();

    @Select("SELECT\n" +
            "\ts.id, \n" +
            "\ts.uuid, \n" +
            "\ts.type, \n" +
            "\ts.caller, \n" +
            "\ts.account_info, \n" +
            "\ts.title, \n" +
            "\ts.contents, \n" +
            "\ts.extra_info,\n" +
            "\ts.create_time \n" +
            "FROM sys_logs s \n" +
            "WHERE \n" +
            "\ts.caller=#{caller} AND \n" +
            "\ts.title=#{title} \n" +
            "ORDER BY s.create_time DESC \n" +
            "LIMIT #{offset}, #{count}")
    List<SystemLogPo> getLogs(String caller, String title, int offset, int count);

    @Select("SELECT\n" +
            "\ts.id, \n" +
            "\ts.uuid, \n" +
            "\ts.type, \n" +
            "\ts.caller, \n" +
            "\ts.account_info, \n" +
            "\ts.title, \n" +
            "\ts.contents, \n" +
            "\ts.extra_info,\n" +
            "\ts.create_time \n" +
            "FROM sys_logs s \n" +
            "WHERE \n" +
            "\ts.caller=#{caller} AND \n" +
            "\ts.title=#{title} AND \n" +
            "\ts.create_time BETWEEN #{begin_time} AND #{end_time} \n" +
            "ORDER BY s.create_time DESC \n" +
            "LIMIT #{offset}, #{count}")
    List<SystemLogPo> getPeriodLogs(String caller, String title,
                              @Param("begin_time")Timestamp beginTime,
                              @Param("end_time")Timestamp endTime,
                              int offset, int count);
}
