package com.vulner.unify_auth.dao;

import com.vulner.unify_auth.bean.dto.AccountRoleMapDto;
import com.vulner.unify_auth.bean.dto.RoleDto;
import com.vulner.unify_auth.bean.po.AccountRolePo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jason
 * @create 2019/12/25
 * @since 1.0.0
 * @description 账号角色映射表的操作（Mybatis）
 */
@Component
public interface AccountRolesDao {
    /**
     * 获取指定账号的所有角色
     * @param accountUuid 账号 UUID
     * @return List<RoleDto> 账号关联的角色列表
     */
    @Select("SELECT\n" +
            "\tr.uuid AS role_uuid,\n" +
            "\tr.name AS role_name,\n" +
            "\tr.alias AS role_alias\n" +
            "FROM account_roles ar\n" +
            "LEFT JOIN roles r ON ar.role_uuid=r.uuid\n" +
            "WHERE ar.account_uuid=#{accountUuid};")
    List<RoleDto> getAccountRoles(@Param("accountUuid") String accountUuid);

    /**
     * 读取指定账号、指定角色的一条关联记录
     * @param accountUuid 账号 UUID
     * @param roleUuid 角色 UUID
     * @return AccountRoleDto 账号角色关联记录
     */
    @Select("SELECT\n" +
            "\tar.account_uuid,\n" +
            "\ta.name AS account_name,\n" +
            "\ta.alias AS account_alias,\n" +
            "\tr.uuid AS role_uuid,\n" +
            "\tr.name AS role_name,\n" +
            "\tr.alias AS role_alias\n" +
            "FROM account_roles ar\n" +
            "LEFT JOIN roles r ON ar.role_uuid=r.uuid\n" +
            "LEFT JOIN accounts a ON ar.account_uuid=a.uuid\n" +
            "WHERE ar.account_uuid=#{accountUuid}\n" +
            "\tAND ar.role_uuid=#{roleUuid}")
    AccountRoleMapDto getAccountRoleMap(@Param("accountUuid") String accountUuid, @Param("roleUuid") String roleUuid);

    /**
     * 添加一条账号角色关联记录
     * @param accountRolePo 账号角色关联记录
     * @return 实际添加的记录数
     */
    @Insert("INSERT INTO account_roles (\n" +
            "\t`uuid`,\n" +
            "\t`account_uuid`,\n" +
            "\t`role_uuid`,\n" +
            "\t`create_time`\n" +
            ")\n" +
            "VALUES(\n" +
            "\t#{uuid},\n" +
            "\t#{account_uuid},\n" +
            "\t#{role_uuid},\n" +
            "\t#{create_time}\n" +
            ")")
    int addAccountRoleMap(AccountRolePo accountRolePo);

    /**
     * 删除账号角色的关联记录
     * @param accountUuid 账号 UUID
     * @param roleUuid 角色 UUID
     * @return 实际删除的记录数
     */
    @Delete("DELETE FROM account_roles ar \n" +
            "WHERE ar.account_uuid=#{accountUuid}\n" +
            "\tAND ar.role_uuid=#{roleUuid}")
    int deleteAccountRoleMap(@Param("accountUuid") String accountUuid, @Param("roleUuid") String roleUuid);

    /**
     * 在账号角色映射表中，删除指定账号关联角色的所有记录
     * @param accountUuid 指定账号的 UUID
     * @return 实际删除的记录数
     */
    @Delete("DELETE FROM account_roles \n" +
            "WHERE account_uuid=#{accountUuid}\n")
    int deleteAllMapsByAccountUuid(@Param("accountUuid") String accountUuid);

    /**
     * 在账号角色映射表中，删除指定角色关联账号的所有记录
     * @param roleUuid 指定角色的 UUID
     * @return 实际删除的记录数
     */
    @Delete("DELETE FROM account_roles \n" +
            "WHERE role_uuid=#{roleUuid}\n")
    int deleteAllMapsByRoleUuid(@Param("roleUuid") String roleUuid);
}
