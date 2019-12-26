package com.vulner.unify_auth.service.helper;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.dao.AccountRolesDao;
import com.vulner.unify_auth.dao.AccountsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordHelper {
    @Autowired
    private AccountsDao accountsDao;

    public PasswdParamsDto increaseFailedAttempt(String accountName) {
        // 拿到用户的PO对象
        AccountPo accountPo = accountsDao.findByAccount(accountName);
        if (accountPo == null) {
            return null;
        }

        PasswdParamsDto paramsDto = new PasswdParamsDto();
        // 取出密码的参数备用
        int max = accountPo.getMax_attempts();
        int attempts = accountPo.getAttempts();
        short locked = accountPo.getLocked();
        if (locked == PwdLockStatusEnum.UNLOCKED.getLocked()) {
            // 密码未锁定时，修改尝试次数或锁定状态
            // 尝试次数加 1
            attempts += 1;
            // 尝试次数超过最大阈值，则锁定密码
            if (attempts >= max) {
                locked = PwdLockStatusEnum.LOCKED.getLocked();
            }
            // 更新密码参数
            paramsDto.setAccount_uuid(accountPo.getUuid());
            paramsDto.setMax_attempts(max);
            paramsDto.setAttempts(attempts);
            paramsDto.setLocked(locked);
            int row = accountsDao.updatePasswdParams(paramsDto);
            if (row != 1) {
                return null;
            }
        }

        return paramsDto;
    }

    public PasswdParamsDto clearFailedAttempt(String accountName) {
        // 拿到用户的PO对象
        AccountPo accountPo = accountsDao.findByAccount(accountName);
        if (accountPo == null) {
            return null;
        }

        // 准备初始化密码参数
        PasswdParamsDto paramsDto = new PasswdParamsDto();
        paramsDto.setAccount_uuid(accountPo.getUuid());
        // 最大尝试次数保持不变
        paramsDto.setMax_attempts(accountPo.getMax_attempts());
        // 已尝试次数清零
        paramsDto.setAttempts(0);
        // 消除密码锁定状态
        paramsDto.setLocked(PwdLockStatusEnum.UNLOCKED.getLocked());

        int row = accountsDao.updatePasswdParams(paramsDto);
        if (row != 1) {
            return null;
        }
        return paramsDto;
    }
}
