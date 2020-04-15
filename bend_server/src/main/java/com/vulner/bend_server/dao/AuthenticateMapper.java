package com.vulner.bend_server.dao;

import com.vulner.bend_server.bean.po.AssetAuthenticatePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticateMapper {

    @Insert("INSERT INTO asset_authenticate ( " +
            " uuid, asset_uuid, sym_key, " +
            " public_key, dev_fingerprint, " +
            " update_time, create_time )" +
            " VALUES" +
            " ( #{uuid}, #{asset_uuid}, #{sym_key}, " +
            " #{public_key},  #{dev_fingerprint}, " +
            " #{update_time}, #{create_time})")
    int addAuthenticate(AssetAuthenticatePo assetAuthenticatePo);

    @Select("SELECT\n" +
            "   id, uuid, asset_uuid, authenticate_flag, sym_key, " +
            "   public_key, dev_fingerprint, update_time, create_time \n" +
            "FROM\n" +
            "	asset_authenticate \n" +
            "WHERE\n" +
            "	asset_uuid = #{asset_uuid} \n" +
            "ORDER BY id DESC \n" +
            "	LIMIT 1")
    AssetAuthenticatePo getAuthenticate(@Param("asset_uuid") String assetUuid);


    @Update("UPDATE asset_authenticate SET \n" +
            "asset_uuid = #{asset_uuid}, \n" +
            "authenticate_flag = #{authenticate_flag}, \n" +
            "sym_key = #{sym_key}, \n" +
            "public_key = #{public_key}, \n" +
            "dev_fingerprint = #{dev_fingerprint}, \n" +
            "update_time = #{update_time} \n" +
            "WHERE uuid = #{uuid}")
    int updAuthenticate(AssetAuthenticatePo assetAuthenticatePo);
}
