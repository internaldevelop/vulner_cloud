package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.AccountStatusEnum;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.ObjUtils;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.bean.dto.AccountPersonalInfoDto;
import com.vulner.unify_auth.bean.dto.AccountRegisterDto;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
public class AccountsManageService {
    @Autowired
    private AccountsDao accountsDao;

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

        return ResponseHelper.success();
    }

    public ResponseBean getAccountInfo(Principal user) {
        if (user == null || Strings.isNullOrEmpty(user.getName())) {
            return ResponseHelper.error("ERROR_INVALID_ACCOUNT");
        }

        // 读取账户信息
        AccountPo accountPo = accountsDao.findByAccount(user.getName());
        if (accountPo == null) {
            return ResponseHelper.error("ERROR_INVALID_ACCOUNT");
        }

        // 清空密码数据
        accountPo.setPassword("");
        accountPo.setSalt("");

        return ResponseHelper.success(accountPo);
    }
}
