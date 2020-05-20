package com.vulner.embed_terminal.dao;

import com.vulner.embed_terminal.bean.dto.AssetAuthenticateDto;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import com.vulner.common.utils.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AssetsMapper {

    @Insert(" INSERT INTO assets \n" +
            "	(uuid, classify, code, name, " +
            "   ip, port, os_type, os_ver, on_line, " +
            "   update_time, create_time) \n" +
            " VALUES \n" +
            "	(#{uuid}, #{classify}, #{code}, #{name}, " +
            "   #{ip}, #{port}, #{os_type}, #{os_ver}, #{on_line}, " +
            "   #{update_time}, #{create_time})")
    int addAssets(AssetsPo assetsPo);

    @Select(" SELECT\n" +
            "	id, uuid, classify,\n" +
            "	CODE, NAME, ip, PORT, os_type,\n" +
            "	os_ver, on_line, expire_time, update_time, create_time \n" +
            " FROM\n" +
            "	assets \n" +
            " ORDER BY id DESC")
    List<AssetsPo> getAssets();

    @Select(" SELECT\n" +
            "	id, uuid, classify,\n" +
            "	CODE, NAME, ip, PORT, os_type,\n" +
            "	os_ver, on_line, expire_time, update_time, create_time \n" +
            " FROM\n" +
            "	assets \n" +
            " WHERE\n" +
            "	uuid = #{uuid} \n" +
            " ORDER BY id DESC \n" +
            " LIMIT 1")
    AssetsPo getAssetsByUuid(@Param("uuid") String uuid);

    @Select("<script>" +
            " SELECT\n" +
            "	id, uuid, classify,\n" +
            "	CODE, NAME, ip, PORT, os_type,\n" +
            "	os_ver, on_line, expire_time, update_time, create_time \n" +
            " FROM\n" +
            "	assets \n" +
            " WHERE 1 = 1 \n" +
            "   <when test='asset_uuid!=null'> AND uuid = #{asset_uuid} </when> " +
            "   <when test='asset_name!=null'> AND name = #{asset_name} </when> " +
            " ORDER BY id DESC \n" +
            " LIMIT 1" +
            "</script>")
    AssetsPo getAssetPo(Map<String, String> params);

    @Select(" SELECT\n" +
            "	id, uuid, classify,\n" +
            "	CODE, NAME, ip, PORT, os_type,\n" +
            "	os_ver, on_line, expire_time, update_time, create_time \n" +
            " FROM\n" +
            "	assets \n" +
            " WHERE\n" +
            "	ip = #{ip} \n" +
            " ORDER BY id DESC \n" +
            " LIMIT 1")
    AssetsPo getAssetsByIp(@Param("ip") String ip);


    @Update(" UPDATE assets \n" +
            "	SET classify = #{classify},\n" +
            "	CODE = #{code},\n" +
            "	NAME = #{name},\n" +
            "	ip = #{ip},\n" +
            "	PORT = #{port},\n" +
            "	os_type = #{os_type},\n" +
            "	os_ver = #{os_ver},\n" +
            "   on_line = #{on_line},\n" +
            "	expire_time = #{expire_time},\n" +
            "	update_time = #{update_time},\n" +
            "	create_time = #{create_time} \n" +
            " WHERE\n" +
            "	uuid = #{uuid}")
    int updAssets(AssetsPo assetsPo);


    @Select("<script>" +
            "SELECT\n" +
            "	a.id, a.uuid, a.classify,\n" +
            "	a.CODE, a.NAME, a.ip, a.PORT,\n" +
            "	a.os_type, a.os_ver, a.on_line, a.expire_time, a.update_time,\n" +
            "	a.create_time, aa.uuid AS auth_uuid,\n" +
            "	aa.authenticate_flag, aa.sym_key, aa.public_key \n" +
            "   <when test='flag==2'>, aa.dev_fingerprint </when> \n" +
            " FROM \n" +
            "	assets a INNER JOIN asset_authenticate aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE 1=1 " +
            " <when test='uuid!=null'> AND a.uuid=#{uuid} </when>"+
            " <when test='name!=null'> AND a.name=#{name} </when>"+
            " <when test='ip!=null'> AND a.ip=#{ip} </when>"+
            " <when test='os_type!=null'> AND a.os_type=#{os_type} </when>"+
            " <when test='classify!=null'> AND a.classify=#{classify} </when>"+
            " <when test='authenticate_flag!=null'> AND aa.authenticate_flag=#{authenticate_flag} </when>"+
            " ORDER BY a.id DESC" +
            " <when test='start!=null and count != null'> LIMIT #{start}, #{count} </when>"+
            "</script>")
    List<AssetAuthenticateDto> getAssetAuth(Map<String, Object> params);

    @Select("<script>" +
            " SELECT COUNT(a.uuid)" +
            " FROM \n" +
            "	assets a INNER JOIN asset_authenticate aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE 1=1 " +
            " <when test='uuid!=null'> AND a.uuid=#{uuid} </when>"+
            " <when test='name!=null'> AND a.name=#{name} </when>"+
            " <when test='ip!=null'> AND a.ip=#{ip} </when>"+
            " <when test='os_type!=null'> AND a.os_type=#{os_type} </when>"+
            " <when test='classify!=null'> AND a.classify=#{classify} </when>"+
            " <when test='authenticate_flag!=null'> AND aa.authenticate_flag=#{authenticate_flag} </when>"+
            "</script>")
    int getAssetAuthCount(Map<String, Object> params);

    @Select("SELECT\n" +
            "	a.id, a.uuid, a.classify,\n" +
            "	a.CODE, a.NAME, a.ip, a.PORT,\n" +
            "	a.os_type, a.os_ver, a.on_line, a.expire_time, a.update_time,\n" +
            "	a.create_time, aa.uuid AS auth_uuid,\n" +
            "	aa.authenticate_flag, aa.sym_key, aa.public_key, \n" +
            "	aa.dev_fingerprint, aa.ciphertext, aa.plaintext, \n" +
            "   aa.create_time AS auth_time \n" +
            " FROM \n" +
            "	assets a INNER JOIN asset_authenticate aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE a.uuid = #{assetUuid} \n" +
            " LIMIT 1")
    AssetAuthenticateDto assetAuthenticateInfo(String assetUuid);

    @Select("<script>" +
            "SELECT\n" +
            "	SUM(c.num) AS all_num,\n" +
            "	SUM(CASE c.classify WHEN 0 THEN c.num ELSE 0 END) as wei_num,\n" +
            "	SUM(CASE c.classify WHEN 1 THEN c.num ELSE 0 END) as bai_num,\n" +
            "	SUM(CASE c.classify WHEN -1 THEN c.num ELSE 0 END) as hei_num,\n" +
            "	SUM(CASE c.on_line WHEN 1 THEN c.num ELSE 0 END) as on_line_num,\n" +
            "	SUM(CASE c.authenticate_flag WHEN 3 THEN c.num ELSE 0 END) as auth_num\n" +
            " FROM (\n" +
            "	SELECT\n" +
            "		a.classify,\n" +
            "		a.on_line,\n" +
            "		aa.authenticate_flag,\n" +
            "		COUNT( a.id ) AS num \n" +
            "	FROM\n" +
            "		assets a\n" +
            "		LEFT JOIN asset_authenticate aa ON a.uuid = aa.asset_uuid \n" +
            "	WHERE 1 = 1 \n" +
            "   <when test='start_time!=null and end_time!=null'> AND (a.create_time BETWEEN CONCAT(#{start_time}, ' 00:00:00') AND CONCAT(#{end_time}, ' 23:59:59')) </when>" +
            "	GROUP BY\n" +
            "		a.classify,\n" +
            "		a.on_line,\n" +
            "		aa.authenticate_flag \n" +
            " ) c" +
            "</script>")
    Map<String, Object> getStatistics(Map<String, Object> params);
}
