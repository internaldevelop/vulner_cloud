package com.vulner.unify_auth.controller;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/uni_auth")
public class AuthUserApi {
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping(value = "/user_info", produces = "application/json")
    public Map<String, Object> userInfo(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return userInfo;
    }

    @GetMapping(value = "/user", produces = "application/json")
    public Principal user(Principal user) {
        return user;
    }

    @DeleteMapping(value = "/exit")
    public ResponseBean revokeToken(@RequestParam("access_token") String accessToken) {
        if (consumerTokenServices.revokeToken(accessToken)) {
            return ResponseHelper.success("注销成功");
        } else {
            return ResponseHelper.error("ERROR_LOGOUT_FAILED");
        }
    }
}
