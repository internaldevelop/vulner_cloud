package com.vulner.bend_server.dao;

import com.vulner.bend_server.bean.dto.AssetAuthenticateDto;
import com.vulner.bend_server.bean.po.AssetsPo;
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
            "	(uuid, empower_flag, code, name, " +
            "   ip, port, os_type, os_ver, " +
            "   update_time, create_time) \n" +
            " VALUES \n" +
            "	(#{uuid}, #{empower_flag}, #{code}, #{name}, " +
            "   #{ip}, #{port}, #{os_type}, #{os_ver}, " +
            "   #{update_time}, #{create_time})")
    int addAssets(AssetsPo assetsPo);

    @Select(" SELECT\n" +
            "	id, uuid, empower_flag,\n" +
            "	CODE, NAME, ip, PORT, os_type,\n" +
            "	os_ver, update_time, create_time \n" +
            " FROM\n" +
            "	assets \n" +
            " ORDER BY id DESC")
    List<AssetsPo> getAssets();

    @Select(" SELECT\n" +
            "	id, uuid, empower_flag,\n" +
            "	CODE, NAME, ip, PORT, os_type,\n" +
            "	os_ver, update_time, create_time \n" +
            " FROM\n" +
            "	assets \n" +
            " WHERE\n" +
            "	uuid = #{uuid} \n" +
            " ORDER BY id DESC \n" +
            " LIMIT 1")
    AssetsPo getAssetsByUuid(@Param("uuid") String uuid);

    @Select(" SELECT\n" +
            "	id, uuid, empower_flag,\n" +
            "	CODE, NAME, ip, PORT, os_type,\n" +
            "	os_ver, update_time, create_time \n" +
            " FROM\n" +
            "	assets \n" +
            " WHERE\n" +
            "	ip = #{ip} \n" +
            " ORDER BY id DESC \n" +
            " LIMIT 1")
    AssetsPo getAssetsByIp(@Param("ip") String ip);


    @Update(" UPDATE assets \n" +
            "	SET empower_flag = #{empower_flag},\n" +
            "	CODE = #{code},\n" +
            "	NAME = #{name},\n" +
            "	ip = #{ip},\n" +
            "	PORT = #{port},\n" +
            "	os_type = #{os_type},\n" +
            "	os_ver = #{os_ver},\n" +
            "	update_time = #{update_time},\n" +
            "	create_time = #{create_time} \n" +
            " WHERE\n" +
            "	uuid = #{uuid}")
    int updAssets(AssetsPo assetsPo);


    @Select("<script>" +
            "SELECT\n" +
            "	a.id, a.uuid, a.empower_flag,\n" +
            "	a.CODE, a.NAME, a.ip, a.PORT,\n" +
            "	a.os_type, a.os_ver, a.update_time,\n" +
            "	a.create_time, aa.uuid AS auth_uuid,\n" +
            "	aa.authenticate_flag, aa.sym_key,\n" +
            "	aa.public_key, aa.dev_fingerprint \n" +
            " FROM \n" +
            "	assets a INNER JOIN asset_authenticate aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE 1=1 " +
            " <when test='uuid!=null'> AND a.uuid=#{uuid} </when>"+
            " <when test='name!=null'> AND a.name=#{name} </when>"+
            " <when test='ip!=null'> AND a.ip=#{ip} </when>"+
            " <when test='os_type!=null'> AND a.os_type=#{os_type} </when>"+
            " <when test='empower_flag!=null'> AND a.empower_flag=#{empower_flag} </when>"+
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
            " <when test='empower_flag!=null'> AND a.empower_flag=#{empower_flag} </when>"+
            " <when test='authenticate_flag!=null'> AND aa.authenticate_flag=#{authenticate_flag} </when>"+
            "</script>")
    int getAssetAuthCount(Map<String, Object> params);

    @Select(" SELECT\n" +
            "	a.id, a.uuid, a.empower_flag,\n" +
            "	a.CODE, a.NAME, a.ip, a.PORT,\n" +
            "	a.os_type, a.os_ver, a.update_time,\n" +
            "	a.create_time, aa.uuid AS auth_uuid,\n" +
            "	aa.authenticate_flag, aa.sym_key,\n" +
            "	aa.public_key, aa.dev_fingerprint \n" +
            " FROM\n" +
            "	assets a INNER JOIN asset_authenticate aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE\n" +
            "	a.uuid = #{uuid} \n" +
            " ORDER BY a.id DESC")
    List<AssetAuthenticateDto> getAssetAuthByUuid(Map<String, Object> params);


}
