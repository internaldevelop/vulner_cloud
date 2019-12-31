package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.bean.dto.AccountRoleDto;
import com.vulner.unify_auth.bean.po.AccountRolePo;
import com.vulner.unify_auth.dao.AccountRolesDao;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.dao.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountRolesMapService {
    @Autowired
    private AccountsDao accountsDao;

    @Autowired
    private RolesDao rolesDao;

    @Autowired
    private AccountRolesDao accountRolesDao;

    private boolean _addAccountRoleMap(String accountUuid, String roleUuid) {
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

    private boolean _delAccountRole(String accountUuid, String roleUuid) {
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

    public boolean addAccountRoleByName(String accountName, String roleName) {
        String accountUuid = accountsDao.getAccountUuidByName(accountName);
        String roleUuid = rolesDao.getRoleUuidByName(roleName);
        return _addAccountRoleMap(accountUuid, roleUuid);
    }

    public ResponseBean addAccountRoleByUuid(String accountUuid, String roleUuid) {
        boolean rv = _addAccountRoleMap(accountUuid, roleUuid);
        if (!rv) {
            return ResponseHelper.error("ERROR_ADD_ACC_ROLE_FAILED");
        }
        return ResponseHelper.success();
    }

    public boolean deleteAccountRoleByName(String accountName, String roleName) {
        String accountUuid = accountsDao.getAccountUuidByName(accountName);
        String roleUuid = rolesDao.getRoleUuidByName(roleName);
        return _delAccountRole(accountUuid, roleUuid);
    }

    public ResponseBean deleteAccountRoleByUuid(String accountUuid, String roleUuid) {
        boolean rv = _delAccountRole(accountUuid, roleUuid);
        if (!rv) {
            return ResponseHelper.error("ERROR_DEL_ACCOUNT_ROLE_FAILED");
        }
        return ResponseHelper.success();
    }

    public boolean deleteAccountAllRolesByUuid(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return false;
        }

        int rows = accountRolesDao.deleteAccountAllRoles(accountUuid);
        return (rows >= 1);
    }

    public ResponseBean fetchAllRolesOfAccount(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }

        // 读取账户所有关联的角色
        List<AccountRoleDto> accountRoles =  accountRolesDao.getAccountRoles(accountUuid);

        // 如果 list 非法，则创建一个空的list
        if (accountRoles == null) {
            accountRoles = new ArrayList<>();
        }

        // 成功返回账户关联的角色列表，或空列表
        return ResponseHelper.success(accountRoles);
    }

}
