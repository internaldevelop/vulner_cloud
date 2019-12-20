package com.vulner.bend_server.controller;

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
    public String hello(){
        return "hello";
    }

    @GetMapping("current_user")
    @PreAuthorize("hasAnyAuthority('current')")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("query")
    @PreAuthorize("hasAnyAuthority('query')")
    public String query() {
        return "具有query权限";
    }

    //    @GetMapping(value = "/oath", produces = "application/json")
//    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('current')")
//    public Object testOauth(){
//        return ResponseHelper.success("Pass oauth authentication.");
//    }
}

//@RestController
//@RequestMapping(value = "/api")
//public class TestApi {
//
//    @GetMapping("hello")
//    @PreAuthorize("hasAnyAuthority('hello')")
//    public String hello(){
//        return "hello";
//    }
//
//    @GetMapping("current")
//    public Principal user(Principal principal) {
//        return principal;
//    }
//
//    @GetMapping("query")
//    @PreAuthorize("hasAnyAuthority('query')")
//    public String query() {
//        return "具有query权限";
//    }
//
////    @GetMapping(value = "/oath", produces = "application/json")
////    @ResponseBody
////    @PreAuthorize("hasAnyAuthority('current')")
////    public Object testOauth(){
////        return ResponseHelper.success("Pass oauth authentication.");
////    }
//
//}
