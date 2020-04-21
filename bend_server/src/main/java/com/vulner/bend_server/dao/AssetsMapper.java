package com.vulner.bend_server.dao;

import com.vulner.bend_server.bean.po.AssetsPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
