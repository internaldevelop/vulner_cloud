package com.vulner.unify_auth.configuration.exception;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.exception.MyAccountNotFoundException;
import com.vulner.unify_auth.service.exception.MyAuthException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;

/**
 * @author Jason
 * @create 2019/12/13
 * @since 1.0.0
 * @description 对web响应异常的转译
 */
public class MyWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

        // Exception: self-defined exception, e.g.:
        // UsernameNotFoundException -> MyAccountNotFoundException
        MyAuthException myAuthEx = (MyAuthException) throwableAnalyzer.getFirstThrowableOfType(MyAuthException.class,
                causeChain);
        if (myAuthEx != null) {
            return myAuthEx.handleResponse();
        }

        // Exception: InvalidGrantException
        // 密码校验失败
        InvalidGrantException grantEx = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class,
                causeChain);
        if (grantEx != null) {

        }

        // Exception: 401
        AuthenticationException authEx = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);
        if (authEx != null) {
            return handleOAuth2Exception(new MyWebResponseExceptionTranslator.UnauthorizedException(e.getMessage(), e));
        }

        // Exception: 403
        AccessDeniedException accessDenyEx = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (accessDenyEx instanceof AccessDeniedException) {
            return handleOAuth2Exception(new MyWebResponseExceptionTranslator.ForbiddenException(accessDenyEx.getMessage(), accessDenyEx));
        }

        // Exception: 405
        HttpRequestMethodNotSupportedException notSupportEx = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(
                HttpRequestMethodNotSupportedException.class, causeChain);
        if (notSupportEx instanceof HttpRequestMethodNotSupportedException) {
            return handleOAuth2Exception(new MyWebResponseExceptionTranslator.MethodNotAllowed(notSupportEx.getMessage(), notSupportEx));
        }

        // Exception: 400
        OAuth2Exception oAuth2Ex = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        if (oAuth2Ex != null) {
            return handleOAuth2Exception(oAuth2Ex);
        }

        // Exception: 500
        return handleOAuth2Exception(new MyWebResponseExceptionTranslator.ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));

    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {

        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        String message=e.getMessage();
        MyOAuth2Exception exception = new MyOAuth2Exception(message,e);

        ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(exception, headers, HttpStatus.valueOf(status));

        return response;

    }

    public void setThrowableAnalyzer(ThrowableAnalyzer throwableAnalyzer) {
        this.throwableAnalyzer = throwableAnalyzer;
    }

    @SuppressWarnings("serial")
    private static class ForbiddenException extends OAuth2Exception {

        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "access_denied";
        }

        @Override
        public int getHttpErrorCode() {
            return 403;
        }

    }

    @SuppressWarnings("serial")
    private static class ServerErrorException extends OAuth2Exception {

        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }

        @Override
        public int getHttpErrorCode() {
            return 500;
        }

    }

    @SuppressWarnings("serial")
    private static class UnauthorizedException extends OAuth2Exception {

        public UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "unauthorized";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }

    }

    @SuppressWarnings("serial")
    private static class MethodNotAllowed extends OAuth2Exception {

        public MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "method_not_allowed";
        }

        @Override
        public int getHttpErrorCode() {
            return 405;
        }

    }
}
