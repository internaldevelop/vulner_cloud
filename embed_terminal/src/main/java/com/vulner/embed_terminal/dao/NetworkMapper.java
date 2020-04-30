package com.vulner.embed_terminal.dao;

import com.vulner.embed_terminal.bean.po.AssetsPo;
import com.vulner.embed_terminal.bean.po.NetworkPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NetworkMapper {

    @Insert("INSERT INTO asset_network ( " +
            " uuid, NAME, mac_address, mtu, " +
            " speed, ipv4, ipv6, bytes_recv, " +
            " bytes_sent, packets_recv, packets_sent, " +
            " asset_uuid, create_time ) " +
            "VALUES ( " +
            " #{uuid}, #{name}, #{mac_address}, #{mtu}, " +
            " #{speed}, #{ipv4}, #{ipv6}, #{bytes_recv}, " +
            " #{bytes_sent}, #{packets_recv}, #{packets_sent}, " +
            " #{asset_uuid}, #{create_time} " +
            ")")
    int addNetwork(NetworkPo networkPo);

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



}
