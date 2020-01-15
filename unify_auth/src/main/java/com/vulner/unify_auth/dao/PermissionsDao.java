package com.vulner.unify_auth.dao;

import com.vulner.common.bean.po.PermissionPo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 权限表的操作（Mybatis）
 */
@Component
public interface PermissionsDao {
    @Select("SELECT\n" +
            "\tp.uuid,\n" +
            "\tp.name,\n" +
            "\tp.description,\n" +
            "\tp.create_time,\n" +
            "\tp.update_time\n" +
            "FROM permissions p\n")
    List<PermissionPo> allPermissions();

    @Select("SELECT\n" +
            "\tp.uuid,\n" +
            "\tp.name,\n" +
            "\tp.description,\n" +
            "\tp.create_time,\n" +
            "\tp.update_time\n" +
            "FROM permissions p\n" +
            "WHERE p.uuid=#{permUuid}")
    PermissionPo serachByUuid(@Param("permUuid") String permUuid);

    @Insert("INSERT INTO permissions (\n" +
            "\t`uuid`,\n" +
            "\t`name`,\n" +
            "\t`description`, \n" +
            "\t`create_time`,\n" +
            "\t`update_time`\n" +
            ")\n" +
            "VALUES(\n" +
            "\t#{uuid},\n" +
            "\t#{name},\n" +
            "\t#{description},\n" +
            "\t#{create_time},\n" +
            "\t#{update_time}\n" +
            ")")
    int addPermission(PermissionPo permissionPo);

    @Update("UPDATE permissions p \n" +
            "SET\n" +
            "\tp.`name`=#{name},\n" +
            "\tp.`description`=#{description},\n" +
            "\tp.`update_time`=#{update_time}\n" +
            "WHERE\n" +
            "\tp.uuid=#{uuid}")
    int updatePermission(PermissionPo permissionPo);

    @Delete("DELETE FROM permissions p \n" +
            "WHERE p.uuid=#{permUuid}\n")
    int deletePermission(@Param("permUuid") String permUuid);

    @Select("SELECT\n" +
            "\tp.`uuid`\n" +
            "FROM permissions p\n" +
            "WHERE p.name=#{permName};")
    String getPermUuidByName(@Param("permName") String permName);

    @Select("SELECT * \n" +
            "FROM permissions p \n" +
            "WHERE p.`name`=#{permName} \n")
    List<PermissionPo> existName(@Param("permName") String permName);

    @Select("SELECT * \n" +
            "FROM permissions p \n" +
            "WHERE p.`name`=#{permName} \n" +
            "AND p.uuid<>#{permUuid}")
    List<PermissionPo> existOtherName(@Param("permUuid") String permUuid, @Param("permName") String permName);
}
