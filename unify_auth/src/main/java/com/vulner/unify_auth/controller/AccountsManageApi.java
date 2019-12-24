package com.vulner.unify_auth.controller;

import com.vulner.unify_auth.service.AccountsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/account_manage")
public class AccountsManageApi {
    @Autowired
    private AccountsManageService accountsManageService;

    @GetMapping(value = "/all", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object getAllAccounts() {
        return accountsManageService.getAllAccounts();
    }

    @PostMapping(value = "/add", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object addAccount() {
        return "OK";
    }

    @DeleteMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object deleteAccount() {
        return "OK";
    }

    @PostMapping(value = "/update", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object updateAccount() {
        return "OK";
    }

    @DeleteMapping(value = "/revoke", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object revokeAccount() {
        return "OK";
    }

    @GetMapping(value = "/activate", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object activateAccount() {
        return "OK";
    }

    @GetMapping(value = "/unlock", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object unlockAccount() {
        return "OK";
    }
}
