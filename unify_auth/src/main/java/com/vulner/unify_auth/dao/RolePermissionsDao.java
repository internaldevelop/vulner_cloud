package com.vulner.unify_auth.dao;

import com.vulner.unify_auth.bean.dto.PermissionDto;
import com.vulner.unify_auth.bean.dto.RolePermMapDto;
import com.vulner.unify_auth.bean.po.RolePermissionPo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 角色权限映射表的操作（Mybatis）
 */

@Component
public interface RolePermissionsDao {
    @Select("SELECT\n" +
            "\tp.uuid AS permission_uuid,\n" +
            "\tp.name AS permission_name,\n" +
            "\tp.description AS permission_desc\n" +
            "FROM role_permissions rp\n" +
            "LEFT JOIN permissions p ON rp.permission_uuid=p.uuid\n" +
            "WHERE rp.role_uuid=#{roleUuid};")
    List<PermissionDto> getPermissions(String roleUuid);

    @Select("SELECT\n" +
            "\trp.role_uuid,\n" +
            "\tr.name AS role_name,\n" +
            "\tr.alias AS role_alias,\n" +
            "\tp.uuid AS permission_uuid,\n" +
            "\tp.name AS permission_name,\n" +
            "\tp.description AS permission_desc\n" +
            "FROM role_permissions rp\n" +
            "LEFT JOIN roles r ON rp.role_uuid=r.uuid\n" +
            "LEFT JOIN permissions p ON rp.permission_uuid=p.uuid\n" +
            "WHERE rp.role_uuid=#{roleUuid}\n" +
            "\tAND rp.permission_uuid=#{permUuid}")
    RolePermMapDto getRolePermMap(String roleUuid, String permUuid);

    @Insert("INSERT INTO role_permissions (\n" +
            "\t`uuid`,\n" +
            "\t`role_uuid`,\n" +
            "\t`permission_uuid`,\n" +
            "\t`create_time`\n" +
            ")\n" +
            "VALUES(\n" +
            "\t#{uuid},\n" +
            "\t#{role_uuid},\n" +
            "\t#{permission_uuid},\n" +
            "\t#{create_time}\n" +
            ")")
    int addRolePermMap(RolePermissionPo rolePermissionPo);

    @Delete("DELETE FROM role_permissions rp \n" +
            "WHERE rp.role_uuid=#{roleUuid}\n" +
            "\tAND rp.permission_uuid=#{permUuid}")
    int deleteRolePermMap(String roleUuid, String permUuid);

    @Delete("DELETE FROM role_permissions rp \n" +
            "WHERE rp.role_uuid=#{roleUuid}\n")
    int deleteAllMapsByRoleUuid(String roleUuid);

    @Delete("DELETE FROM role_permissions rp \n" +
            "WHERE rp.permission_uuid=#{permUuid}\n")
    int deleteAllMapsByPermUuid(String permUuid);
}
