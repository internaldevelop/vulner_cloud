package com.vulner.unify_auth.configuration.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author Jason
 * @create 2019/12/19
 * @since 1.0.0
 * @description 扩展 OAuth2 异常处理
 */
@JsonSerialize(using = MyOAuthExceptionJacksonSerializer.class)
public class MyOAuth2Exception extends OAuth2Exception {

    public MyOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public MyOAuth2Exception(String msg) {
        super(msg);
    }
}

