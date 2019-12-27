package com.vulner.unify_auth.service.listener;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.service.helper.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author Jason
 * @create 2019/12/23
 * @since 1.0.0
 * @description 登陆成功监听和处理
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private AccountsDao accountsDao;
    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        Authentication authentication = authenticationSuccessEvent.getAuthentication();
        Object details = authentication.getDetails();

        if (details instanceof WebAuthenticationDetails) {
            // 客户端认证，不处理
            return;
        }

        // 处理账号认证成功事件
        if (details instanceof LinkedHashMap &&
                ((LinkedHashMap)details).containsKey("grant_type") &&
                ((String)(((LinkedHashMap)details).get("grant_type"))).equals("password")) {
            // 只有密码校验时才清除尝试次数
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            passwordHelper.clearFailedAttempt(username);
        }
    }
}
