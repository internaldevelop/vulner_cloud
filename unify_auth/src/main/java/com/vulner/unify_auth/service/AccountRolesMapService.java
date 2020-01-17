package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.bean.dto.AccountRoleMapDto;
import com.vulner.unify_auth.bean.dto.AccountRolesDto;
import com.vulner.unify_auth.bean.dto.RoleDto;
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

    @Autowired
    private AccountsManageService accountsManageService;

    private boolean _addAccountRoleMap(String accountUuid, String roleUuid) {
        if (Strings.isNullOrEmpty(accountUuid) || Strings.isNullOrEmpty(roleUuid)) {
            return false;
        }

        // 检查是否已存在账号角色对应关系
        AccountRoleMapDto accountRoleMapDto = accountRolesDao.getAccountRoleMap(accountUuid, roleUuid);
        if (accountRoleMapDto != null) {
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
        AccountRoleMapDto accountRoleMapDto = accountRolesDao.getAccountRoleMap(accountUuid, roleUuid);
        if (accountRoleMapDto == null) {
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

    public boolean deleteAllMapsByAccountUuid(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return false;
        }

        int rows = accountRolesDao.deleteAllMapsByAccountUuid(accountUuid);
        return (rows >= 1);
    }

    public boolean deleteAllMapsByRoleUuid(String roleUuid) {
        if (Strings.isNullOrEmpty(roleUuid)) {
            return false;
        }

        int rows = accountRolesDao.deleteAllMapsByRoleUuid(roleUuid);
        return (rows >= 1);
    }

    public ResponseBean fetchAllRolesOfAccount(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }

        // 查找账户信息
        AccountPo accountPo = accountsManageService.searchAccountByUuid(accountUuid);
        if (accountPo == null) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST", accountUuid);
        }

        // 读取账户所有关联的角色
        List<RoleDto> accountRoles =  accountRolesDao.getAccountRoles(accountUuid);

        // 如果 list 非法，则创建一个空的list
        if (accountRoles == null) {
            accountRoles = new ArrayList<>();
        }

        // 构造响应数据（账户信息 + 角色列表）
        AccountRolesDto accountRolesDto = new AccountRolesDto();
        accountRolesDto.setAccount_uuid(accountUuid);
        accountRolesDto.setAccount_name(accountPo.getName());
        accountRolesDto.setAccount_alias(accountPo.getAlias());
        accountRolesDto.setRoles(accountRoles);

        // 成功返回账户关联的角色列表，或空列表
        return ResponseHelper.success(accountRolesDto);
    }

    public ResponseBean setAccountRoles(String accountUuid, String accountName, String roleUuids, String roleNames) {
        if (Strings.isNullOrEmpty(accountUuid) && Strings.isNullOrEmpty(accountName)) {
            return ResponseHelper.blankParams("账号 UUID 或账号名");
        }
        if (Strings.isNullOrEmpty(roleUuids) && Strings.isNullOrEmpty(roleNames)) {
            return ResponseHelper.blankParams("角色 UUID 列表或角色名列表");
        }

        // 解析角色列表
        List<String> roleUuidList = new ArrayList<>();
        if (Strings.isNullOrEmpty(roleUuids)) {
            String[] roleNameList = roleNames.split(",");
            for (String roleName : roleNameList) {
                String roleUuid = rolesDao.getRoleUuidByName(roleName);
                if (!Strings.isNullOrEmpty(roleUuid)) {
                    roleUuidList.add(roleUuid);
                }
            }
        } else {
            String[] uuidList = roleUuids.split(",");
            for (String uuid : uuidList) {
                int count = rolesDao.getCountByUuid(uuid);
                if (count > 0) {
                    roleUuidList.add(uuid);
                }
            }
        }

        // 删除指定账户的所有角色关联记录
        if (Strings.isNullOrEmpty(accountUuid)) {
            accountUuid = accountsDao.getAccountUuidByName(accountName);
        }
        accountRolesDao.deleteAllMapsByAccountUuid(accountUuid);

        // 增加指定账户的角色关联记录
        for (String roleUuid : roleUuidList) {
            _addAccountRoleMap(accountUuid, roleUuid);
        }

        // 成功返回
        return ResponseHelper.success();
    }

}
