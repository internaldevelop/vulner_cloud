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
 * @description 认证授权失败的异常基础类定义
 */
public abstract class MyAuthException extends AuthenticationException {

    public MyAuthException(String msg) {
        super(msg);
    }

    public MyAuthException(String msg, Throwable t) {
        super(msg, t);
    }

    public abstract String getOAuth2ErrorCode();

    public abstract String getSystemErrorCode();

    public abstract String getSummary();

    public int getHttpErrorCode() {
        return 200;
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        return headers;
    }

    public ResponseEntity handleResponse() {
        HttpHeaders headers = this.getHeaders();
        int status = this.getHttpErrorCode();
        ResponseBean body = ResponseHelper.error(this.getSystemErrorCode(), this.getSummary());
        ResponseEntity response = new ResponseEntity(body, headers, HttpStatus.valueOf(status));

        // 日志记录口令锁定
        SysLogger sysLogger = (SysLogger) SpringUtil.getBean("sysLogger");
        sysLogger.fail("登录", this.getSummary());

        return response;
    }
}
