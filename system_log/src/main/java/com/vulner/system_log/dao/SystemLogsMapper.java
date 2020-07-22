package com.vulner.system_log.dao;

import com.vulner.system_log.bean.po.SystemLogBackupPo;
import com.vulner.system_log.bean.po.SystemLogInfoConfigPo;
import com.vulner.system_log.bean.po.SystemLogPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
            "WHERE s.status = 1 AND \n" +
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
            "WHERE s.status = 1 AND \n" +
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

    @Select("<script>" +
            "SELECT\n" +
            "   s.id, \n" +
            "   s.uuid, \n" +
            "   s.type, \n" +
            "   s.caller, \n" +
            "   s.account_info, \n" +
            "   s.title, \n" +
            "   s.contents, \n" +
            "   s.extra_info,\n" +
            "   s.create_time \n" +
            "FROM sys_logs s \n" +
            "WHERE 1=1 \n" +
            " <when test='uuid !=null'> AND uuid = #{uuid} </when>"+
            " <when test='status!=null'> AND s.status =#{status} </when>"+
            " <when test='caller!=null'> AND s.caller=#{caller} </when>"+
            " <when test='caller!=null'> AND s.title=#{title} </when>"+
            " <when test='begin_time !=null and end_time != null'> AND s.create_time BETWEEN CONCAT(#{begin_time}, ' 00:00:00') AND CONCAT(#{end_time}, ' 23:59:59') </when>"+
            "ORDER BY s.create_time DESC \n" +
            " <when test='offset !=null and count != null'> LIMIT #{offset}, #{count} </when>"+
            "</script>")
    List<SystemLogPo> getLogList(Map<String, Object> params);

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
            "WHERE s.status = 1 AND \n" +
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
            "WHERE s.status = 1 AND \n" +
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

    @Update("UPDATE sys_logs \n" +
            " SET STATUS = 0 \n" +
            " WHERE\n" +
            "	uuid = #{uuid}")
    int uptLog(@Param("uuid")String uuid);

    @Update(" UPDATE sys_log_info_config \n" +
            "	SET is_display = #{is_display},\n" +
            "	update_time = #{update_time}\n" +
            " WHERE\n" +
            "	uuid = #{uuid}")
    int uptLogInfoConfig(SystemLogInfoConfigPo slicPo);

    @Update(" UPDATE sys_log_info_config \n" +
            "	SET is_display = #{is_display},\n" +
            "	update_time = #{update_time}\n" +
            " WHERE is_default = #{is_default}")
    int delLogInfoConfig(SystemLogInfoConfigPo slicPo);

    @Insert("INSERT INTO sys_log_backup ( uuid, user_uuid, user_name, file_url, file_name, begin_time, end_time, create_time )\n" +
            "VALUES\n" +
            "	(#{uuid}, #{user_uuid}, #{user_name}, #{file_url}, #{file_name}, #{begin_time}, #{end_time}, #{create_time})")
    int addLogBackup(SystemLogBackupPo sysLogBackupPo);

    @Select("<script>" +
            " SELECT COUNT(1) \n" +
            " FROM sys_log_backup \n" +
            " WHERE 1 = 1 \n" +
            " ORDER BY id DESC \n" +
            " <when test='start !=null and count != null'> LIMIT #{start}, #{count} </when>"+
            "</script>")
    int getBackupCount(Map<String, Object> params);


    @Select("<script>" +
            " SELECT\n" +
            "	id,\n" +
            "	uuid,\n" +
            "	user_uuid,\n" +
            "	user_name,\n" +
            "	file_url,\n" +
            "	file_name,\n" +
            "	begin_time,\n" +
            "	end_time,\n" +
            "	create_time\n" +
            " FROM sys_log_backup \n" +
            " WHERE 1 = 1 \n" +
            " <when test='uuid !=null'> AND uuid = #{uuid} </when>"+
            " ORDER BY id DESC \n" +
            " <when test='start !=null and count != null'> LIMIT #{start}, #{count} </when>"+
            "</script>")
    List<SystemLogBackupPo> getBackupList(Map<String, Object> params);

    @Select("<script>" +
            " SELECT\n" +
            "	id,\n" +
            "	uuid,\n" +
            "	user_uuid,\n" +
            "	user_name,\n" +
            "	file_url,\n" +
            "	file_name,\n" +
            "	begin_time,\n" +
            "	end_time,\n" +
            "	create_time\n" +
            " FROM sys_log_backup \n" +
            " WHERE 1 = 1 \n" +
            " <when test='uuid !=null'> AND uuid = #{uuid} </when>"+
            " ORDER BY id DESC \n" +
            " LIMIT 1"+
            "</script>")
    SystemLogBackupPo getBackupInfo(Map<String, Object> params);
}
