package com.vulner.unify_auth.service.listener;

import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.enumeration.PwdLockStatusEnum;
import com.vulner.unify_auth.bean.dto.PasswdParamsDto;
import com.vulner.unify_auth.dao.AccountsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

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

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        Authentication authentication = authenticationSuccessEvent.getAuthentication();
        Object details = authentication.getDetails();
        if (details instanceof WebAuthenticationDetails) {
            // 客户端认证，不处理
            return;
        }

        // 处理账号认证成功事件
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        // 拿到用户的PO对象
        AccountPo accountPo = accountsDao.findByAccount(username);
        if (accountPo == null) {
            return;
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

        accountsDao.updatePasswdParams(paramsDto);
    }
}
