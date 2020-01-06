package com.vulner.unify_auth.service.event;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.common.global.MyConst;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.service.AccountsManageService;
import com.vulner.unify_auth.service.exception.MyAccountLockedException;
import com.vulner.unify_auth.service.exception.MyAccountPwdVerifyException;
import com.vulner.unify_auth.service.helper.PasswordHelper;
import com.vulner.unify_auth.service.helper.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Jason
 * @create 2019/12/23
 * @since 1.0.0
 * @description 登陆失败监听和处理
 */
@Component
public class AuthenticationFailEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private AccountsManageService accountsManageService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    private SessionHelper sessionHelper;

    private void _saveAccountInfoIntoSession(UserDetails userDetails) {
        Map<String, String[]> paramsMap = httpServletRequest.getParameterMap();
        HttpSession session = httpServletRequest.getSession();
        if (paramsMap == null || session == null) {
            return;
        }

        // 保存 token 到会话中
        if (paramsMap.containsKey(MyConst.ACCESS_TOKEN)) {
            String accessToken = paramsMap.get(MyConst.ACCESS_TOKEN)[0];
            session.setAttribute(MyConst.ACCESS_TOKEN, accessToken);
        }

        // 获取账户信息
        ResponseBean response = accountsManageService.getAccountInfoByName(userDetails.getUsername());

        // 保存账户信息到会话中
        if (ResponseHelper.isSuccess(response)) {
            AccountPo accountPo = (AccountPo)response.getPayload();
            if (accountPo != null) {
                session.setAttribute(MyConst.ACCOUNT_UUID, accountPo.getUuid());
                session.setAttribute(MyConst.ACCOUNT_NAME, accountPo.getName());
                session.setAttribute(MyConst.ACCOUNT_ALIAS, accountPo.getAlias());
            }
        }
    }

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

        // 保存 token 和账号信息到 Session 中
        sessionHelper.saveAccountInfoIntoSession(authentication);

        // 抛出密码校验异常
        if (passwdParams.getLocked() == PwdLockStatusEnum.UNLOCKED.getLocked()) {
            throw new MyAccountPwdVerifyException(username);
        } else {
            throw new MyAccountLockedException(username);
        }
    }
}
