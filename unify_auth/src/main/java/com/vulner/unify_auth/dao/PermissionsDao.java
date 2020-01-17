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
    /**
     * 读取所有权限的记录
     * @return PermissionPo 列表
     */
    @Select("SELECT\n" +
            "\tp.uuid,\n" +
            "\tp.name,\n" +
            "\tp.description,\n" +
            "\tp.create_time,\n" +
            "\tp.update_time\n" +
            "FROM permissions p\n")
    List<PermissionPo> allPermissions();

    /**
     * 读取指定权限的记录
     * @param permUuid 指定权限的 UUID
     * @return PermissionPo
     */
    @Select("SELECT\n" +
            "\tp.uuid,\n" +
            "\tp.name,\n" +
            "\tp.description,\n" +
            "\tp.create_time,\n" +
            "\tp.update_time\n" +
            "FROM permissions p\n" +
            "WHERE p.uuid=#{permUuid}")
    PermissionPo serachByUuid(@Param("permUuid") String permUuid);

    /**
     * 获取指定权限 UUID 的记录数量
     * @param permUuid  指定权限 UUID
     * @return 1(存在该权限) or 0 (该权限不存在)
     */
    @Select("SELECT COUNT(1)\n" +
            "FROM permissions p\n" +
            "WHERE p.uuid=#{permUuid}")
    int getCountByUuid(@Param("permUuid") String permUuid);

    /**
     * 添加一条权限
     * @param permissionPo 权限对象 PermissionPo
     * @return 实际插入记录数
     */
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

    /**
     * 更新一条权限
     * @param permissionPo 权限对象 PermissionPo
     * @return 实际更新记录数
     */
    @Update("UPDATE permissions p \n" +
            "SET\n" +
            "\tp.`name`=#{name},\n" +
            "\tp.`description`=#{description},\n" +
            "\tp.`update_time`=#{update_time}\n" +
            "WHERE\n" +
            "\tp.uuid=#{uuid}")
    int updatePermission(PermissionPo permissionPo);

    /**
     * 删除一条权限
     * @param permUuid 指定权限 UUID
     * @return 实际删除记录数
     */
    @Delete("DELETE FROM permissions p \n" +
            "WHERE p.uuid=#{permUuid}\n")
    int deletePermission(@Param("permUuid") String permUuid);

    /**
     * 从权限名称获取该权限的 UUID
     * @param permName 权限名称
     * @return 权限的 UUID
     */
    @Select("SELECT\n" +
            "\tp.`uuid`\n" +
            "FROM permissions p\n" +
            "WHERE p.name=#{permName};")
    String getPermUuidByName(@Param("permName") String permName);

    /**
     * 获取指定权限名称的的权限记录列表
     * @param permName 待查询的权限名称
     * @return 权限记录列表
     */
    @Select("SELECT * \n" +
            "FROM permissions p \n" +
            "WHERE p.`name`=#{permName} \n")
    List<PermissionPo> existName(@Param("permName") String permName);

    /**
     * 除指定 UUID 的权限外，获取指定权限名称的其他权限记录列表
     * @param permUuid 指定权限的 UUID
     * @param permName 待查询的权限名称
     * @return
     */
    @Select("SELECT * \n" +
            "FROM permissions p \n" +
            "WHERE p.`name`=#{permName} \n" +
            "AND p.uuid<>#{permUuid}")
    List<PermissionPo> existOtherName(@Param("permUuid") String permUuid, @Param("permName") String permName);
}
