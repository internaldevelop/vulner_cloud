package com.vulner.unify_auth.controller;

import com.vulner.unify_auth.service.AccountRolesMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( value = "account_role_map", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountRoleMapApi {
    @Autowired
    private AccountRolesMapService accountRolesMapService;

    @GetMapping(value = "all")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object allAccountRoles(@RequestParam("account_uuid") String accountUuid) {
        return accountRolesMapService.fetchAllRolesOfAccount(accountUuid);
    }

    @PostMapping(value = "add")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object addAccountRole(@RequestParam("account_uuid") String accountUuid,
                          @RequestParam("role_uuid") String roleUuid) {
        return accountRolesMapService.addAccountRoleByUuid(accountUuid, roleUuid);
    }

    @DeleteMapping(value = "delete")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object deleteAccountRole(@RequestParam("account_uuid") String accountUuid,
                             @RequestParam("role_uuid") String roleUuid) {
        return accountRolesMapService.deleteAccountRoleByUuid(accountUuid, roleUuid);
    }
}
