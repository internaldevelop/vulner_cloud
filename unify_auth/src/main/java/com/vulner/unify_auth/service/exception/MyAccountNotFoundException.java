package com.vulner.unify_auth.service.exception;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.logger.SysLogger;
import com.vulner.unify_auth.util.SpringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        String msg = String.format("账号（%s）不存在！", this.getMessage());

        return msg;
    }

}
