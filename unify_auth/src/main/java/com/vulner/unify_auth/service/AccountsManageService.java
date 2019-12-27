package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.AccountStatusEnum;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.bean.dto.AccountPersonalInfoDto;
import com.vulner.unify_auth.bean.dto.AccountRegisterDto;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.service.helper.AccountHelper;
import com.vulner.unify_auth.service.helper.PasswordHelper;
import com.vulner.unify_auth.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AccountsManageService {
    @Autowired
    private AccountsDao accountsDao;

    @Autowired
    private RolesManageService rolesManageService;

    @Autowired
    private PasswordHelper passwordHelper;

    public AccountPo getAccountByUuid(String uuid) {
        String accountName = accountsDao.getAccountNameByUuid(uuid);
        if (Strings.isNullOrEmpty(accountName)) {
            return null;
        }

        return accountsDao.findByAccount(accountName);
    }

    public ResponseBean getAllAccounts() {
        List<AccountPo> accountsList = accountsDao.fetchAllAccounts();
        if (accountsList == null || accountsList.size() == 0) {
            return ResponseHelper.error("ERROR_NONE_ACCOUNTS");
        } else {
            return ResponseHelper.success(accountsList);
        }
    }

    public ResponseBean updateAccountPersonalInfo(AccountPersonalInfoDto personalInfo) {
        String accountUuid = personalInfo.getUuid();

        // 获取 AccountPo
        AccountPo accountPo = getAccountByUuid(accountUuid);
        if (accountPo == null) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST");
        }

        // 将 personalInfo 拷贝到 AccountPo 对象中
        BeanUtils.copyProperties(personalInfo, accountPo);

        // 更新 AccountPo 到数据库中
        int row = accountsDao.updateAccountPersonalInfo(accountPo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_UPDATE_FAILED");
        }

        return ResponseHelper.success();
    }

    public ResponseBean registerAccount(AccountRegisterDto registerDto) {
        // 检查账号是否已存在，或重名
        AccountPo accountPo = accountsDao.findByAccount(registerDto.getName());
        if (accountPo != null) {
            return ResponseHelper.error("ERROR_ACCOUNT_EXIST");
        }

        // 将 registerDto 拷贝到 AccountPo 对象中
        accountPo = new AccountPo();
        BeanUtils.copyProperties(registerDto, accountPo);

        // 设置新账号的uuid
        accountPo.setUuid(StringUtils.generateUuid());
        // 设置新账号的密码盐
        accountPo.setSalt(PasswordUtil.getSalt());
        // 设置新账号的密码尝试次数
        accountPo.setMax_attempts(PasswordUtil.getMaxAttempts());
        accountPo.setAttempts(0);
        // 设置新账号的密码未锁定
        accountPo.setLocked(PwdLockStatusEnum.UNLOCKED.getLocked());
        // 设置新账号的状态
        accountPo.setStatus(AccountStatusEnum.ACTIVE.getStatus());
        // 设置新账号的创建时间
        accountPo.setCreate_time(TimeUtils.getCurrentDate());

        int row = accountsDao.addAccount(accountPo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_CREATE_FAILED");
        }

        // 默认设置账号为 guest 角色
        if (!rolesManageService.addAccountRoleByName(registerDto.getName(), rolesManageService.getDefaultRole())) {
            return ResponseHelper.error("ERROR_FAIL_GUEST_ROLE");
        }

        // 返回响应前清空账户隐私信息
        AccountHelper.clearAccountSecretInfo(accountPo);
        return ResponseHelper.success(accountPo);
    }

    public ResponseBean getAccountInfo(String accountName) {
        // 读取账户信息
        AccountPo accountPo = accountsDao.findByAccount(accountName);
        if (accountPo == null) {
            return ResponseHelper.error("ERROR_INVALID_ACCOUNT");
        }

        // 返回响应前清空账户隐私信息
        AccountHelper.clearAccountSecretInfo(accountPo);

        return ResponseHelper.success(accountPo);
    }

    public ResponseBean deleteAccountByUuid(String accountUuid) {
        // 删除所有的账号角色映射
        boolean rv = rolesManageService.deleteAccountAllRolesByUuid(accountUuid);

        // 删除账号
        int row = accountsDao.deleteAccount(accountUuid);
        if (row != 1) {
            return ResponseHelper.error("ERROR_DEL_ACCOUNT_FAILED");
        }

        return ResponseHelper.success(accountUuid);
    }

    public ResponseBean activateAccount(String accountUuid) {
        int row = accountsDao.updateStatus(accountUuid, AccountStatusEnum.ACTIVE.getStatus());
        if (row != 1) {
            return ResponseHelper.error("ERROR_ACTIVATE_ACCOUNT_FAILED");
        }
        return ResponseHelper.success(accountUuid);
    }

    public ResponseBean revokeAccount(String accountUuid) {
        int row = accountsDao.updateStatus(accountUuid, AccountStatusEnum.DEACTIVE.getStatus());
        if (row != 1) {
            return ResponseHelper.error("ERROR_REVOKE_ACCOUNT_FAILED");
        }
        return ResponseHelper.success(accountUuid);
    }

    public ResponseBean unlockAccountPassword(String accountUuid) {
        String accountName = accountsDao.getAccountNameByUuid(accountUuid);
        if (Strings.isNullOrEmpty(accountName)) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST");
        }

        // 清除尝试次数和锁定状态
        PasswdParamsDto passwdParams = passwordHelper.clearFailedAttempt(accountName);
        if (passwdParams == null) {
            return ResponseHelper.error("ERROR_UNLOCK_PASSWORD_FAILED");
        }

        return ResponseHelper.success(passwdParams);
    }

    public ResponseBean changePassword(String accountName, String oldPwd, String newPwd) {
        // 判断用户名是否有效
        if (Strings.isNullOrEmpty(accountName)) {
            return ResponseHelper.error("ERROR_ILLEGAL_USER_NAME");
        }

        // 读取用户的账户信息
        AccountPo accountPo = accountsDao.findByAccount(accountName);
        if (accountPo == null) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST", accountName);
        }

        // 密码锁定时，不允许修改
        if (accountPo.getLocked() == PwdLockStatusEnum.LOCKED.getLocked()) {
            return ResponseHelper.error("ERROR_PASSWORD_LOCKED");
        }

        // 判断新旧密码是否完全相同
        if (Strings.isNullOrEmpty(oldPwd)) {
            return ResponseHelper.error("ERROR_PARAMS_MISSING", "必须输入旧密码进行身份验证");
        }
        if (Strings.isNullOrEmpty(newPwd)) {
            return ResponseHelper.error("ERROR_PARAMS_MISSING", "新密码不能为空");
        }
        if (oldPwd.equals(newPwd)) {
            return ResponseHelper.error("ERROR_NEW_PWD_EQUALS_OLD");
        }

        // 校验旧密码
        if (!accountPo.getPassword().equals(oldPwd)) {
            PasswdParamsDto passwdParams = passwordHelper.increaseFailedAttempt(accountName);
            return ResponseHelper.error("ERROR_INVALID_PASSWORD", passwdParams);
        }

        // 校验成功后，清除尝试次数
        PasswdParamsDto passwdParamsDto = passwordHelper.clearFailedAttempt(accountName);

        // 修改新密码
        int row = accountsDao.updatePassword(accountPo.getUuid(), newPwd);
        if (row != 1) {
            return ResponseHelper.error("ERROR_CHANGE_PWD_FAILED");
        }

        return ResponseHelper.success(passwdParamsDto);
    }
}
