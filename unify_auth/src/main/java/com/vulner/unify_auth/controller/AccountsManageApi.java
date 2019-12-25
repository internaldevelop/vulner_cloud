package com.vulner.unify_auth.controller;

import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.bean.dto.AccountPersonalInfoDto;
import com.vulner.unify_auth.bean.dto.AccountRegisterDto;
import com.vulner.unify_auth.service.AccountsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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

    @GetMapping(value = "/self", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('current-user')")
    public @ResponseBody
    Object getSelfAccountInfo(Principal user) {
        return accountsManageService.getAccountInfo(user);
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
    Object updateAccount(@Valid AccountPersonalInfoDto personalInfoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.invalidParams(bindingResult);
        }
        return accountsManageService.updateAccountPersonalInfo(personalInfoDto);
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
