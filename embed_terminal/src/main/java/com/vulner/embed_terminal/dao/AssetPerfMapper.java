package com.vulner.embed_terminal.dao;

import com.vulner.embed_terminal.bean.po.AssetPerfPo;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AssetPerfMapper {

    @Insert("INSERT INTO asset_perf_data ( \n" +
            "   uuid, asset_uuid, cpu_free_percent, cpu_used_percent, \n" +
            "   memory_free_percent, memory_used_percent, \n" +
            "   disk_free_percent, disk_used_percent, \n" +
            "   packets_recv, packets_sent, bytes_recv, bytes_sent, \n" +
            "   create_time )\n" +
            " VALUES ( \n" +
            "   #{uuid}, #{asset_uuid}, #{cpu_free_percent}, #{cpu_used_percent}, \n" +
            "   #{memory_free_percent}, #{memory_used_percent}, \n" +
            "   #{disk_free_percent}, #{disk_used_percent}, \n" +
            "   #{packets_recv}, #{packets_sent}, #{bytes_recv}, #{bytes_sent}, \n" +
            "   #{create_time})")
    int addAssetPerfData(AssetPerfPo assetsPo);

    @Select("<script>" +
            " SELECT \n" +
            "   id, uuid, asset_uuid, cpu_free_percent, cpu_used_percent, \n" +
            "	memory_free_percent, memory_used_percent, \n" +
            "	disk_free_percent, disk_used_percent, \n" +
            "	packets_recv, packets_sent,\n" +
            "	bytes_recv, bytes_sent,\n" +
            "	create_time \n" +
            " FROM \n" +
            "	asset_perf_data \n" +
            " WHERE 1=1 " +
            " <when test='asset_uuid!=null'> AND asset_uuid=#{asset_uuid} </when>"+
            " ORDER BY id DESC" +
            " <when test='start!=null and count != null'> LIMIT #{start}, #{count} </when>"+
            "</script>")
    List<AssetPerfPo> getDataByAssetUuid(Map<String, Object> params);

    @Select("<script>" +
            " SELECT COUNT(uuid)" +
            " FROM \n" +
            "	asset_perf_data \n" +
            " WHERE 1=1 " +
            " <when test='asset_uuid!=null'> AND asset_uuid=#{asset_uuid} </when>"+
            "</script>")
    int getDataByAssetUuidCount(Map<String, Object> params);
}
