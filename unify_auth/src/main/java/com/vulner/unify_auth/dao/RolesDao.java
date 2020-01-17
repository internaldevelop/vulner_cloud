package com.vulner.unify_auth.dao;

import com.vulner.common.bean.po.RolePo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 角色表的操作（Mybatis）
 */
@Component
public interface RolesDao {
    /**
     * 根据角色名获取角色的 UUID
     * @param name 角色名
     * @return 角色 UUID
     */
    @Select("SELECT\n" +
            "\tr.`uuid`\n" +
            "FROM roles r\n" +
            "WHERE r.name=#{name};")
    String getRoleUuidByName(@Param("name") String name);

    /**
     * 根据角色化名获取角色的 UUID
     * @param alias 角色化名
     * @return 角色 UUID
     */
    @Select("SELECT\n" +
            "\tr.`uuid`\n" +
            "FROM roles r\n" +
            "WHERE r.alias=#{alias};")
    String getRoleUuidByAlias(@Param("alias") String alias);

    /**
     * 获取指定角色 UUID 的记录数量
     * @param roleUuid  角色 UUID
     * @return 1(存在该角色) or 0 (该角色不存在)
     */
    @Select("SELECT COUNT(1)\n" +
            "FROM roles r\n" +
            "WHERE r.`uuid`=#{roleUuid};")
    int getCountByUuid(@Param("roleUuid") String roleUuid);

    /**
     * 读取所有角色
     * @return 角色记录 LIST
     */
    @Select("SELECT\n" +
            "\tr.`id`, \n" +
            "\tr.`uuid`, \n" +
            "\tr.`name`, \n" +
            "\tr.`alias`, \n" +
            "\tr.`valid`, \n" +
            "\tr.`create_time`, \n" +
            "\tr.`update_time`\n" +
            "FROM roles r\n")
    List<RolePo> fetchAllRoles();

    /**
     * 获取指定 UUID 的角色
     * @param roleUuid 角色 UUID
     * @return 角色记录
     */
    @Select("SELECT\n" +
            "\tr.`id`, \n" +
            "\tr.`uuid`, \n" +
            "\tr.`name`, \n" +
            "\tr.`alias`, \n" +
            "\tr.`valid`, \n" +
            "\tr.`create_time`, \n" +
            "\tr.`update_time`\n" +
            "FROM roles r\n" +
            "WHERE r.`uuid`=#{roleUuid};")
    RolePo searchByUuid(@Param("roleUuid") String roleUuid);

    /**
     * 增加角色记录
     * @param rolePo 角色 PO 对象
     * @return 实际插入记录数
     */
    @Insert("INSERT INTO roles (\n" +
            "\t`uuid`,\n" +
            "\t`name`,\n" +
            "\t`alias`, \n" +
            "\t`valid`,\n" +
            "\t`create_time`,\n" +
            "\t`update_time`\n" +
            ")\n" +
            "VALUES(\n" +
            "\t#{uuid},\n" +
            "\t#{name},\n" +
            "\t#{alias},\n" +
            "\t#{valid},\n" +
            "\t#{create_time},\n" +
            "\t#{update_time}\n" +
            ")")
    int addRole(RolePo rolePo);

    /**
     * 删除指定 UUID 的角色
     * @param roleUuid 角色 UUID
     * @return 实际删除的记录数
     */
    @Delete("DELETE FROM roles r \n" +
            "WHERE r.uuid=#{roleUuid}\n")
    int deleteRole(@Param("roleUuid") String roleUuid);

    /**
     * 更新角色记录
     * @param rolePo 角色 PO 对象
     * @return 实际更新记录数
     */
    @Update("UPDATE roles r \n" +
            "SET\n" +
            "\tr.`name`=#{name},\n" +
            "\tr.`alias`=#{alias},\n" +
            "\tr.`update_time`=#{update_time}\n" +
            "WHERE\n" +
            "\tr.uuid=#{uuid}")
    int updateRoleName(RolePo rolePo);

    /**
     * 检查给定的角色名称或化名是否已存在
     * @param roleName 待检查的角色名称
     * @param roleAlias 待检查的角色化名
     * @return 角色 PO 对象的 LIST
     */
    @Select("SELECT * \n" +
            "FROM roles r \n" +
            "WHERE r.`name`=#{roleName} \n" +
            "OR r.`alias`=#{roleAlias}")
    List<RolePo> existNameOrAlias(@Param("roleName") String roleName, @Param("roleAlias") String roleAlias);

    /**
     * 除指定 UUID 的角色外，检查给定的角色名称或化名是否已存在
     * @param roleUuid 排除检查的角色记录
     * @param roleName 待检查的角色名称
     * @param roleAlias 待检查的角色化名
     * @return 角色 PO 对象的 LIST
     */
    @Select("SELECT * \n" +
            "FROM roles r \n" +
            "WHERE (r.`name`=#{roleName} \n" +
            "\tOR r.`alias`=#{roleAlias}) \n" +
            "AND r.uuid<>#{roleUuid}")
    List<RolePo> existOtherNameOrAlias(@Param("roleUuid") String roleUuid, @Param("roleName") String roleName, @Param("roleAlias") String roleAlias);
}
