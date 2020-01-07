package com.vulner.unify_auth.service.exception;

import com.alibaba.fastjson.JSONObject;
import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.logger.SysLogger;
import com.vulner.unify_auth.util.SpringUtil;
import com.vulner.unify_auth.dao.AccountsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Jason
 * @create 2019/12/23
 * @since 1.0.0
 * @description 账户密码校验失败的异常定义
 */
public class MyAccountPwdVerifyException extends MyAuthException  {
    private static final long serialVersionUID = 1L;

//    @Autowired
//    private AccountsDao accountsDao;
//    @Autowired
//    private SysLogger sysLogger;

    public MyAccountPwdVerifyException(String msg) {
        super(msg);
    }

    public MyAccountPwdVerifyException(String msg, Throwable t) {
        super(msg, t);
    }

    public String getOAuth2ErrorCode() {
        return "invalid_password";
    }

    public String getSystemErrorCode() {
        return "ERROR_INVALID_PASSWORD";
    }

    public String getSummary() {
        return "密码错误！";
    }

    public ResponseEntity handleResponse() {
        String accountName = this.getMessage();

        // 取账户的密码参数
        AccountsDao accountsDao = (AccountsDao)SpringUtil.getBean("accountsDao");
        AccountPo accountPo = accountsDao.findByAccount(accountName);
        int maxAttempts = accountPo.getMax_attempts();
        int attempts = accountPo.getAttempts();
        int remains = maxAttempts - attempts;

        // 提示信息
        String msg = String.format("账号（%s）密码校验失败，剩余%d次尝试次数（最大尝试次数为%d）。", accountName, remains, maxAttempts);

        // 返回数据体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", msg);
        jsonObject.put("remain", remains);
        jsonObject.put("max", maxAttempts);
        ResponseBean body = ResponseHelper.error(this.getSystemErrorCode(), jsonObject);

        // 头
        HttpHeaders headers = this.getHeaders();

        // 组织响应实体
        int status = this.getHttpErrorCode();
        ResponseEntity response = new ResponseEntity(body, headers, HttpStatus.valueOf(status));

        // 日志记录口令校验失败
        SysLogger sysLogger = (SysLogger)SpringUtil.getBean("sysLogger");
        sysLogger.fail("登录", msg);

        return response;
    }
}
