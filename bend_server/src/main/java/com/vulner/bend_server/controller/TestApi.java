package com.vulner.bend_server.controller;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class TestApi {

    @GetMapping("hello")
    @PreAuthorize("hasAnyAuthority('hello')")
    @ResponseBody
    public ResponseBean hello(){
        return ResponseHelper.success("hello权限已通过");
    }

    @GetMapping("current_user")
    @PreAuthorize("hasAnyAuthority('current user')")
    @ResponseBody
    public ResponseBean user(Principal principal) {
        return ResponseHelper.success(principal);
    }

    @GetMapping("query")
    @PreAuthorize("hasAnyAuthority('query')")
    public ResponseBean query() {
        return ResponseHelper.success("具有query权限");
    }

    //    @GetMapping(value = "/oath", produces = "application/json")
//    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('current')")
//    public Object testOauth(){
//        return ResponseHelper.success("Pass oauth authentication.");
//    }
}
