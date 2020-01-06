package com.vulner.unify_auth.service.helper;

import com.alibaba.fastjson.JSONObject;
import com.vulner.common.bean.po.AccountPo;
import com.vulner.common.global.MyConst;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.AccountsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class SessionHelper {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private AccountsManageService accountsManageService;
    @Autowired
    private AccessTokenHelper accessTokenHelper;

    public String getSessionAttribute(JSONObject jsonObject, String attribute) {
        String value = (String) httpServletRequest.getSession().getAttribute(attribute);
        if (value == null) {
            value = "";
        }
        jsonObject.put(attribute, value);
        return value;
    }

    public void saveAccountInfoIntoSession(Authentication authentication) {
        Map<String, String[]> paramsMap = httpServletRequest.getParameterMap();
        HttpSession session = httpServletRequest.getSession();
        if (authentication == null ||paramsMap == null || session == null) {
            // 只处理认证的成功和失败
            return;
        }

        // 保存 token 到会话中
        String accessToken = null;
        if (!authentication.isAuthenticated()) {
            // 身份认证中，而且认证失败
            accessToken = "";
        } else if (paramsMap.containsKey(MyConst.ACCESS_TOKEN)) {
            // 参数中含有 ACCESS_TOKEN，非身份认证指令的处理中
//            accessToken = paramsMap.get(MyConst.ACCESS_TOKEN)[0];
            return;
        } else {
            // 参数中不含 ACCESS_TOKEN，身份认证指令的处理中，而且认证成功
            accessToken = accessTokenHelper.getAccessToken(httpServletRequest, authentication).getValue();
        }
        session.setAttribute(MyConst.ACCESS_TOKEN, accessToken);

        // 获取账户名称
        String accountName;
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            accountName = userDetails.getUsername();
        } else {
            accountName = authentication.getPrincipal().toString();
        }

        // 在会话中保存账户信息
        _updateAccountInfo(session, accountName);
    }

    private void _updateAccountInfo(HttpSession session, String accountName) {
        ResponseBean response = accountsManageService.getAccountInfoByName(accountName);

        // 保存账户信息到会话中
        if (ResponseHelper.isSuccess(response)) {
            AccountPo accountPo = (AccountPo)response.getPayload();
            if (accountPo != null) {
                session.setAttribute(MyConst.ACCOUNT_UUID, accountPo.getUuid());
                session.setAttribute(MyConst.ACCOUNT_NAME, accountPo.getName());
                session.setAttribute(MyConst.ACCOUNT_ALIAS, accountPo.getAlias());
                return;
            }
        }

        // 找不到的账号
        session.setAttribute(MyConst.ACCOUNT_NAME, accountName);
        session.setAttribute(MyConst.ACCOUNT_UUID, "");
        session.setAttribute(MyConst.ACCOUNT_ALIAS, "");
    }

    public void saveFailedAccountInfoIntoSession(String accountName) {
        HttpSession session = httpServletRequest.getSession();
        if (session == null) {
            return;
        }

        // 失败的处理中，保存空的 token 信息
        session.setAttribute(MyConst.ACCESS_TOKEN, "");

        // 在会话中保存账户信息
        _updateAccountInfo(session, accountName);
    }
}
