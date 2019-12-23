package com.vulner.unify_auth.service.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Jason
 * @create 2019/12/23
 * @since 1.0.0
 * @description 账号锁定的异常类定义
 */
public class MyAccountLockedException extends MyAuthException {
    private static final long serialVersionUID = 1L;

    public MyAccountLockedException(String msg) {
        super(msg);
    }

    public MyAccountLockedException(String msg, Throwable t) {
        super(msg, t);
    }

    public String getOAuth2ErrorCode() {
        return "account_locked";
    }

    public String getSystemErrorCode() {
        return "ERROR_PASSWORD_LOCKED";
    }

    public String getSummary() {
        return "账户已锁定，请联系系统管理员解锁！";
    }
}
