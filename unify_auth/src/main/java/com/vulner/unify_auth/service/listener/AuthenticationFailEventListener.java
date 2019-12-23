package com.vulner.unify_auth.service.listener;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.service.exception.MyAccountLockedException;
import com.vulner.unify_auth.service.exception.MyAccountPwdVerifyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * @author Jason
 * @create 2019/12/23
 * @since 1.0.0
 * @description 登陆失败监听和处理
 */
@Component
public class AuthenticationFailEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private AccountsDao accountsDao;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        Authentication authentication = authenticationFailureBadCredentialsEvent.getAuthentication();
        Object details = authentication.getDetails();
        if (details instanceof WebAuthenticationDetails) {
            // 客户端认证，不处理
            return;
        }

        // 处理账号认证失败事件
        String username = authentication.getPrincipal().toString();
        // 拿到用户的PO对象
        AccountPo accountPo = accountsDao.findByAccount(username);
        if (accountPo == null) {
            return;
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
            accountsDao.updatePasswdParams(paramsDto);
        }

        // 抛出密码校验异常
        if (locked == PwdLockStatusEnum.UNLOCKED.getLocked()) {
            throw new MyAccountPwdVerifyException(username);
        } else {
            throw new MyAccountLockedException(username);
        }
    }
}
