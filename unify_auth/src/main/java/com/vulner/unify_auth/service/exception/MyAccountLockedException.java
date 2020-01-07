package com.vulner.unify_auth.service.exception;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.logger.SysLogger;
import com.vulner.unify_auth.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Jason
 * @create 2019/12/23
 * @since 1.0.0
 * @description 账号锁定的异常类定义
 */
public class MyAccountLockedException extends MyAuthException {
    private static final long serialVersionUID = 1L;
//    @Autowired
//    private SysLogger sysLogger;

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
        String msg = String.format("账号（%s）密码已锁定，请联系系统管理员解锁！", this.getMessage());

        return msg;
    }

}
