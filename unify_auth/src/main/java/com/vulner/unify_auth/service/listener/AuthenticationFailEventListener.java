package com.vulner.unify_auth.service.listener;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.service.exception.MyAccountLockedException;
import com.vulner.unify_auth.service.exception.MyAccountPwdVerifyException;
import com.vulner.unify_auth.service.helper.PasswordHelper;
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
    @Autowired
    private PasswordHelper passwordHelper;

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
        PasswdParamsDto passwdParams = passwordHelper.increaseFailedAttempt(username);
        if (passwdParams == null) {
            return;
        }

        // 抛出密码校验异常
        if (passwdParams.getLocked() == PwdLockStatusEnum.UNLOCKED.getLocked()) {
            throw new MyAccountPwdVerifyException(username);
        } else {
            throw new MyAccountLockedException(username);
        }
    }
}
