package com.vulner.embed_terminal.dao;

import com.vulner.embed_terminal.bean.dto.AssetAuthenticateDto;
import com.vulner.embed_terminal.bean.po.AssetAuthenticatePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
            "plaintext = #{plaintext}, \n" +
            "ciphertext = #{ciphertext}, \n" +
            "signature = #{signature}, \n" +
            "update_time = #{update_time} \n" +
            "WHERE uuid = #{uuid}")
    int updAuthenticate(AssetAuthenticatePo assetAuthenticatePo);

    @Insert("INSERT INTO asset_authenticate_record ( \n" +
            "   uuid, asset_uuid, authenticate_flag, \n" +
            "   sym_key, public_key, dev_fingerprint, \n" +
            "   plaintext, ciphertext, signature, create_time) \n" +
            " VALUES \n" +
            "   (#{uuid}, #{asset_uuid}, #{authenticate_flag}, \n" +
            "   #{sym_key}, #{public_key}, #{dev_fingerprint}, \n" +
            "   #{plaintext}, #{ciphertext}, #{signature}, #{create_time})")
    int addAuthenticateRecord(AssetAuthenticatePo assetAuthenticatePo);

    @Select("<script>" +
            " SELECT\n" +
            "	a.id, a.uuid, a.classify,\n" +
            "	a.CODE, a.NAME, a.ip, a.PORT,\n" +
            "	a.os_type, a.os_ver, a.expire_time, a.update_time,\n" +
            "	a.create_time, aa.uuid AS auth_uuid,\n" +
            "	aa.authenticate_flag, aa.sym_key, aa.public_key, \n" +
            "	aa.dev_fingerprint, aa.ciphertext, aa.plaintext, \n" +
            "   aa.create_time AS auth_time" +
            " FROM \n" +
            "	assets a INNER JOIN asset_authenticate_record aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE 1=1 " +
            " <when test='asset_uuid!=null'> AND a.uuid=#{asset_uuid} </when>"+
            " ORDER BY a.id DESC" +
            " <when test='start!=null and count != null'> LIMIT #{start}, #{count} </when>"+
            "</script>")
    List<AssetAuthenticateDto> getRecord(Map<String, Object> params);

    @Select("<script>" +
            " SELECT COUNT(a.uuid) \n" +
            " FROM \n" +
            "	assets a INNER JOIN asset_authenticate_record aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE 1=1 " +
            " <when test='asset_uuid!=null'> AND a.uuid=#{asset_uuid} </when>"+
            " <when test='start!=null and count != null'> LIMIT #{start}, #{count} </when>"+
            "</script>")
    int getRecordCount(Map<String, Object> params);

    @Select("SELECT\n" +
            "	a.id, a.uuid, a.classify,\n" +
            "	a.CODE, a.NAME, a.ip, a.PORT,\n" +
            "	a.os_type, a.os_ver, a.expire_time, a.update_time,\n" +
            "	a.create_time, aa.uuid AS auth_uuid,\n" +
            "	aa.authenticate_flag, aa.sym_key, aa.public_key, \n" +
            "	aa.dev_fingerprint, aa.ciphertext, aa.plaintext, \n" +
            "   aa.create_time AS auth_time \n" +
            " FROM \n" +
            "	assets a INNER JOIN asset_authenticate_record aa ON a.uuid = aa.asset_uuid \n" +
            " WHERE aa.uuid=#{authUuid} \n" +
            " LIMIT 1")
    AssetAuthenticateDto authenticateRecordInfo(String authUuid);
}
