package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.netflix.discovery.converters.Auto;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.bean.dto.AccountRoleDto;
import com.vulner.unify_auth.bean.po.AccountRolePo;
import com.vulner.unify_auth.dao.AccountRolesDao;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.dao.RolesDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolesManageService {
    @Autowired
    AccountsDao accountsDao;

    @Autowired
    RolesDao rolesDao;

    @Autowired
    AccountRolesDao accountRolesDao;

    public String getDefaultRole() {
        return "ROLE_GUEST";
    }

    public boolean addAccountRoleByName(String accountName, String roleName) {
        String accountUuid = accountsDao.getAccountUuidByName(accountName);
        String roleUuid = rolesDao.getRoleUuidByName(roleName);
        return addAccountRoleByUuid(accountUuid, roleUuid);
    }

    public boolean addAccountRoleByUuid(String accountUuid, String roleUuid) {
        if (Strings.isNullOrEmpty(accountUuid) || Strings.isNullOrEmpty(roleUuid)) {
            return false;
        }

        // 检查是否已存在账号角色对应关系
        AccountRoleDto accountRoleDto = accountRolesDao.getAccountRoleMap(accountUuid, roleUuid);
        if (accountRoleDto != null) {
            return false;
        }

        // 准备账号角色对应关系
        AccountRolePo accountRolePo = new AccountRolePo();
        accountRolePo.setAccount_uuid(accountUuid);
        accountRolePo.setRole_uuid(roleUuid);
        accountRolePo.setUuid(StringUtils.generateUuid());
        accountRolePo.setCreate_time(TimeUtils.getCurrentDate());

        // 添加一条账号角色对应关系
        int row = accountRolesDao.addAccountRoleMap(accountRolePo);
        return (row == 1);
    }

    public boolean deleteAccountRoleByName(String accountName, String roleName) {
        String accountUuid = accountsDao.getAccountUuidByName(accountName);
        String roleUuid = rolesDao.getRoleUuidByName(roleName);
        return deleteAccountRoleByUuid(accountUuid, roleUuid);
    }

    public boolean deleteAccountRoleByUuid(String accountUuid, String roleUuid) {
        if (Strings.isNullOrEmpty(accountUuid) || Strings.isNullOrEmpty(roleUuid)) {
            return false;
        }

        // 检查是否存在账号角色对应关系，如果不存在，则认定成功删除
        AccountRoleDto accountRoleDto = accountRolesDao.getAccountRoleMap(accountUuid, roleUuid);
        if (accountRoleDto == null) {
            return true;
        }

        // 删除该条对应关系
        int row = accountRolesDao.deleteAccountRoleMap(accountUuid, roleUuid);
        return (row == 1);
    }

    public boolean deleteAccountAllRolesByUuid(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return false;
        }

        int rows = accountRolesDao.deleteAccountAllRoles(accountUuid);
        return (rows >= 1);
    }

}
