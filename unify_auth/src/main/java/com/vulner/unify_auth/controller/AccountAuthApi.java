package com.vulner.unify_auth.controller;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.AccountsManageService;
import com.vulner.unify_auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/account_auth")
public class AccountAuthApi {
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AccountsManageService accountsManageService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping(value = "/test_info", produces = "application/json")
    public @ResponseBody
    Object userInfo(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return ResponseHelper.success(userInfo);
    }

    // 不需要封装返回数据
    @GetMapping(value = "/current", produces = "application/json")
    public @ResponseBody
    Principal user(Principal user) {
        return user;
    }

    @DeleteMapping(value = "/exit")
    public @ResponseBody
    ResponseBean revokeToken(@RequestParam("access_token") String accessToken) {
        if (consumerTokenServices.revokeToken(accessToken)) {
            return ResponseHelper.success("注销成功");
        } else {
            return ResponseHelper.error("ERROR_LOGOUT_FAILED");
        }
    }
}
