package com.vulner.embed_terminal.dao;

import com.alibaba.fastjson.JSONObject;
import com.vulner.embed_terminal.bean.po.AssetDataPacketPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AssetDataPacketMapper {

    @Insert("INSERT INTO asset_data_packet ( \n" +
            "   uuid, asset_uuid, transport_protocol, \n" +
            "   app_protocol, direction, source_ip, source_port, \n" +
            "   dest_ip, dest_port, parse_time, src_data, create_time )\n" +
            " VALUES ( \n" +
            "   #{uuid}, #{asset_uuid}, #{transport_protocol}, \n" +
            "   #{app_protocol}, #{direction}, #{source_ip}, #{source_port}, \n" +
            "   #{dest_ip}, #{dest_port}, #{parse_time}, #{src_data}, #{create_time})")
    int addAssetDataPacketData(AssetDataPacketPo assetDataPacketPo);


    @Select("<script>" +
            " SELECT\n" +
            "	id, uuid, asset_uuid, transport_protocol,\n" +
            "	app_protocol, direction, source_ip, source_port,\n" +
            "	dest_ip, dest_port, parse_time, src_data, create_time \n" +
            " FROM\n" +
            "	asset_data_packet \n" +
            " WHERE 1 = 1 \n" +
            "   <when test='asset_uuid!=null'> AND asset_uuid=#{asset_uuid} </when>" +
            "   <when test='transport_protocol!=null'> AND transport_protocol=#{transport_protocol} </when>" +
            "   <when test='start_time!=null and end_time!=null'> AND (create_time BETWEEN CONCAT(#{start_time}, ' 00:00:00') AND CONCAT(#{end_time}, ' 23:59:59')) </when>" +
            "   ORDER BY id DESC \n" +
            "   <when test='start!=null and count != null'> LIMIT #{start}, #{count} </when>"+
            "</script>")
    List<AssetDataPacketPo> getPacketDataList(Map<String, Object> params);

    @Select("<script>" +
            " SELECT\n" +
            "	count(id) \n" +
            " FROM\n" +
            "	asset_data_packet \n" +
            " WHERE 1 = 1 \n" +
            "   <when test='asset_uuid!=null'> AND asset_uuid=#{asset_uuid} </when>" +
            "   <when test='transport_protocol!=null'> AND transport_protocol=#{transport_protocol} </when>" +
            "   <when test='start_time!=null and end_time!=null'> AND (create_time BETWEEN CONCAT(#{start_time}, ' 00:00:00') AND CONCAT(#{end_time}, ' 23:59:59')) </when>" +
            "</script>")
    int getPacketDataCount(Map<String, Object> params);

    @Select("<script>" +
            " SELECT\n" +
            "	asset_uuid,\n" +
            "	(SELECT name FROM assets WHERE uuid = asset_uuid) asset_name,\n" +
            "	(SELECT ip FROM assets WHERE uuid = asset_uuid) asset_ip,\n" +
            "	SUM( CASE a.direction WHEN 1 THEN a.num ELSE 0 END ) AS sent_count,\n" +
            "	SUM( CASE a.direction WHEN 2 THEN a.num ELSE 0 END ) AS recv_count \n" +
            " FROM ( \n" +
            "   SELECT \n" +
            "       d.asset_uuid, d.direction, COUNT(d.id) num \n" +
            "   FROM \n" +
            "       asset_data_packet d \n" +
            "       INNER JOIN assets e ON d.asset_uuid = e.uuid" +
            "   WHERE 1 = 1 \n" +
            "       <when test='start_time!=null and end_time!=null'> AND (d.create_time BETWEEN CONCAT(#{start_time}, ' 00:00:00') AND CONCAT(#{end_time}, ' 23:59:59')) </when>" +
            "   GROUP BY asset_uuid, direction \n" +
            "   ) a \n" +
            " GROUP BY\n" +
            "	a.asset_uuid\n" +
            "</script>")
    List<Map<String, Object>> getPacketStatistics(Map<String, Object> params);
}
