package com.vulner.unify_auth.service.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Jason
 * @create 2019/12/23
 * @since 1.0.0
 * @description 账号不存在的异常类定义
 */
public class MyAccountNotFoundException extends MyAuthException {
    private static final long serialVersionUID = 1L;

    public MyAccountNotFoundException(String msg) {
        super(msg);
    }

    public MyAccountNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public String getOAuth2ErrorCode() {
        return "invalid_user_name";
    }

    public String getSystemErrorCode() {
        return "ERROR_ACCOUNT_NOT_EXIST";
    }

    public String getSummary() {
        return "用户不存在";
    }
}
