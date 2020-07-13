package com.vulner.system_log.dao;

import com.vulner.system_log.bean.po.SystemLogInfoConfigPo;
import com.vulner.system_log.bean.po.SystemLogPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
    List<SystemLogPo> getLogs(@Param("caller") String caller,
                              @Param("title") String title,
                              @Param("offset") int offset,
                              @Param("count") int count);

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
    List<SystemLogPo> getPeriodLogs(@Param("caller") String caller,
                                    @Param("title") String title,
                                    @Param("begin_time")Timestamp beginTime,
                                    @Param("end_time")Timestamp endTime,
                                    @Param("offset") int offset,
                                    @Param("count") int count);

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
            "\t(#{type}=0 OR s.type=#{type}) AND \n" +
            "\ts.caller LIKE '%${caller}%' AND \n" +
            "\ts.account_info LIKE '%${accountName}%' AND \n" +
            "\ts.account_info LIKE '%${accountAlias}%' AND \n" +
            "\ts.title LIKE '%${title}%' AND \n" +
            "\ts.create_time BETWEEN #{timeFrom} AND #{timeTo} \n" +
            "ORDER BY s.create_time DESC \n" +
            "LIMIT #{offset}, #{count}")
    List<SystemLogPo> searchByFilters(@Param("type") int type,
                                      @Param("caller") String caller,
                                      @Param("accountName") String accountName,
                                      @Param("accountAlias") String accountAlias,
                                      @Param("title") String title,
                                      @Param("timeFrom") Timestamp timeFrom,
                                      @Param("timeTo") Timestamp timeTo,
                                      @Param("offset") int offset,
                                      @Param("count") int count);

    @Select("SELECT count(1)\n" +
            "FROM sys_logs s \n" +
            "WHERE \n" +
            "\t(#{type}=0 OR s.type=#{type}) AND \n" +
            "\ts.caller LIKE '%${caller}%' AND \n" +
            "\ts.account_info LIKE '%${accountName}%' AND \n" +
            "\ts.account_info LIKE '%${accountAlias}%' AND \n" +
            "\ts.title LIKE '%${title}%' AND \n" +
            "\ts.create_time BETWEEN #{timeFrom} AND #{timeTo} \n")
    int countByFilters(@Param("type") int type,
                       @Param("caller") String caller,
                       @Param("accountName") String accountName,
                       @Param("accountAlias") String accountAlias,
                       @Param("title") String title,
                       @Param("timeFrom") Timestamp timeFrom,
                       @Param("timeTo") Timestamp timeTo);

    @Select("<script>" +
            " SELECT\n" +
            "	id,\n" +
            "	uuid,\n" +
            "	log_field,\n" +
            "	log_field_desc,\n" +
            "	is_display,\n" +
            "	is_default,\n" +
            "	update_time,\n" +
            "	create_time \n" +
            " FROM\n" +
            "	sys_log_info_config \n" +
            " WHERE 1=1 " +
            " <when test='uuid!=null'> AND uuid=#{uuid} </when>" +
            " <when test='log_field!=null'> AND log_field=#{log_field} </when>" +
            " ORDER BY id DESC \n" +
            " LIMIT 1" +
            "</script>")
    SystemLogInfoConfigPo getLogInfoConfigInfo(@Param("uuid")String uuid, @Param("log_field")String log_field);

    @Select(" SELECT\n" +
            "	id,\n" +
            "	uuid,\n" +
            "	log_field,\n" +
            "	log_field_desc,\n" +
            "	is_display,\n" +
            "	is_default,\n" +
            "	update_time,\n" +
            "	create_time \n" +
            " FROM\n" +
            "	sys_log_info_config \n" +
            " ORDER BY id DESC")
    List<SystemLogInfoConfigPo> getLogInfoConfig();

    @Insert("INSERT INTO sys_log_info_config( \n" +
            " uuid, log_field, log_field_desc, is_display, is_default, update_time, create_time) \n" +
            "VALUES ( \n" +
            "#{uuid}, #{log_field}, #{log_field_desc}, #{is_display}, #{is_default}, #{update_time, jdbcType=TIMESTAMP}, #{create_time, jdbcType=TIMESTAMP}) ")
    int addLogInfoConfig(SystemLogInfoConfigPo systemLogInfoConfigPo);

    @Update(" UPDATE sys_log_info_config \n" +
            "	SET is_display = #{is_display},\n" +
            "	update_time = #{update_time}\n" +
            " WHERE\n" +
            "	uuid = #{uuid}")
    int uptLogInfoConfig(SystemLogInfoConfigPo slicPo);
}
