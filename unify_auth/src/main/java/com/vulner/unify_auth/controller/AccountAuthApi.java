package com.vulner.unify_auth.controller;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.AccountsManageService;
import com.vulner.unify_auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 账号认证授权 API 接口
 */
@RestController
@RequestMapping(value = "/account_auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountAuthApi {
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AccountsManageService accountsManageService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    /**
     * 系统测试：返回当前认证的用户信息
     * @param user 当前认证的用户
     * @return 自定义的用户信息
     */
    @GetMapping(value = "/test_info")
    public @ResponseBody
    Object userInfo(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return ResponseHelper.success(userInfo);
    }

    /**
     * 返回当前认证用户的信息，即 access_token 对应的用户
     * 用户对象原封不动返回，不要做数据变换或数据封装，其它基于 SpringSecurity 的系统才能识别
     * @param user access_token 对应的用户
     * @return access_token 对应的用户信息
     */
    @GetMapping(value = "/current")
    public @ResponseBody
    Principal user(Principal user) {
        return user;
    }

    /**
     * 用户注销，回收 access_token
     * @param accessToken 已认证用户的 access_token
     * @return ResponseBean
     */
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
