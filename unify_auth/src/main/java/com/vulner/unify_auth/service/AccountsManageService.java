package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.dto.AccountDto;
import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.AccountStatusEnum;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.DateFormat;
import com.vulner.common.utils.ObjUtils;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.bean.dto.AccountPersonalInfoDto;
import com.vulner.unify_auth.bean.dto.AccountRegisterDto;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.bean.po.LicenseExpirePo;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.dao.LicenseDao;
import com.vulner.unify_auth.service.helper.AccountHelper;
import com.vulner.unify_auth.service.helper.PasswordHelper;
import com.vulner.unify_auth.service.helper.RolesHelper;
import com.vulner.unify_auth.service.helper.SessionHelper;
import com.vulner.unify_auth.service.logger.SysLogger;
import com.vulner.unify_auth.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Component
public class AccountsManageService {
    @Autowired
    private AccountsDao accountsDao;

    @Autowired
    private AccountRolesMapService accountRolesMapService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private SysLogger sysLogger;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private LicenseDao licenseDao;

    private String _uuid2Name(String accountUuid) {
        String accountName = accountsDao.getAccountNameByUuid(accountUuid);
        return accountName;
    }

    private String _name2Uuid(String accountName) {
        String accountUuid = accountsDao.getAccountUuidByName(accountName);
        return accountUuid;
    }

    public AccountDto searchAccountByUuid(String uuid, String accountName) {
        Date now = new Date();

        if (!StringUtils.isValid(accountName)) {
            accountName = _uuid2Name(uuid);

        }
        if (!StringUtils.isValid(accountName)) {
            return null;
        }

        AccountDto accountDto = accountsDao.findByAccount(accountName);
        LicenseExpirePo liExpirePo = licenseDao.getExpireInfoByUuid(uuid);
        String expireFlag = "0";  // 过期标识 0:过期; 1:未过期
        if (liExpirePo != null && liExpirePo.getExpire_time() != null && now.compareTo(liExpirePo.getExpire_time()) < 1) {
            expireFlag = "1";
        }
        accountDto.setExpire_flag(expireFlag);

        return accountDto;
    }

    public ResponseBean getAccountInfoByName(String accountName) {
        // 读取账户信息
//        AccountPo accountPo = accountsDao.findByAccount(accountName);
        AccountDto accountDto = searchAccountByUuid(null, accountName);
        if (accountDto == null) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST");
        }

        // 返回响应前清空账户隐私信息
        AccountHelper.clearAccountSecretInfo(accountDto);

        return ResponseHelper.success(accountDto);
    }

    public ResponseBean getAccountInfoByUuid(String accountUuid) {
        // 读取账户信息
        AccountDto accountDto = searchAccountByUuid(accountUuid, null);
        if (accountDto == null) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST");
        }

        // 返回响应前清空账户隐私信息
        AccountHelper.clearAccountSecretInfo(accountDto);

        return ResponseHelper.success(accountDto);
    }

    public ResponseBean getAllAccounts() {
        List<AccountPo> accountsList = accountsDao.fetchAllAccounts();
        if (ObjUtils.nullOrEmptyList(accountsList)) {
            return ResponseHelper.error("ERROR_NONE_ACCOUNTS");
        } else {
            return ResponseHelper.success(accountsList);
        }
    }

    public ResponseBean updateAccountPersonalInfo(AccountPersonalInfoDto personalInfo) {
        String accountUuid = personalInfo.getUuid();

        // 获取 AccountPo
        AccountPo accountPo = searchAccountByUuid(accountUuid, null);
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
        if (!accountRolesMapService.addAccountRoleByName(registerDto.getName(), RolesHelper.getDefaultRole())) {
            return ResponseHelper.error("ERROR_FAIL_GUEST_ROLE");
        }

        // 返回响应前清空账户隐私信息
        AccountHelper.clearAccountSecretInfo(accountPo);
        return ResponseHelper.success(accountPo);
    }

    public ResponseBean deleteAccountByUuid(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }

        // 删除所有的账号角色映射
        boolean rv = accountRolesMapService.deleteAllMapsByAccountUuid(accountUuid);

        // 删除账号
        int row = accountsDao.deleteAccount(accountUuid);
        if (row != 1) {
            return ResponseHelper.error("ERROR_DEL_ACCOUNT_FAILED");
        }

        return ResponseHelper.success(accountUuid);
    }

    public ResponseBean activateAccount(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }

        int row = accountsDao.updateStatus(accountUuid, AccountStatusEnum.ACTIVE.getStatus());
        if (row != 1) {
            return ResponseHelper.error("ERROR_ACTIVATE_ACCOUNT_FAILED");
        }
        return ResponseHelper.success(accountUuid);
    }

    public ResponseBean revokeAccount(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }

        int row = accountsDao.updateStatus(accountUuid, AccountStatusEnum.DEACTIVE.getStatus());
        if (row != 1) {
            return ResponseHelper.error("ERROR_REVOKE_ACCOUNT_FAILED");
        }
        return ResponseHelper.success(accountUuid);
    }

    public ResponseBean unlockAccountPassword(String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }

        String accountName = _uuid2Name(accountUuid);
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

    public ResponseBean getAccountUuid(String accountName) {
        if (Strings.isNullOrEmpty(accountName)) {
            return ResponseHelper.blankParams("账号名称");
        }

        String accountUuid = _name2Uuid(accountName);
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.error("ERROR_ACCOUNT_NOT_EXIST");
        }

        return ResponseHelper.success(accountUuid);
    }

    public ResponseBean accountLogout(String accessToken, Principal user) {
        String accountName = user.getName();
        String msg = String.format("账号（%s）登出", accountName);
        String title = "登出";

        // 会话中保存 token 和账号信息
        sessionHelper.saveTokenAndAccountIntoSession(accessToken, accountName);

        // 回收身份令牌
        boolean rv = consumerTokenServices.revokeToken(accessToken);
        if (rv) {
            msg += "成功";
            sysLogger.success(title, msg);
            return ResponseHelper.success(msg);
        } else {
            msg += "失败";
            sysLogger.fail(title, msg);
            return ResponseHelper.error("ERROR_LOGOUT_FAILED", msg);
        }
    }
}
