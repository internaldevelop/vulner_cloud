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

    /**
     * 返回指定账户所有关联的角色
     * @param accountUuid 指定账户的 UUID
     * @return ResponseBean.payload: AccountRolesDto
     */
    @GetMapping(value = "all")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object allAccountRoles(@RequestParam("account_uuid") String accountUuid) {
        return accountRolesMapService.fetchAllRolesOfAccount(accountUuid);
    }

    /**
     * 对账户增加一条关联的角色
     * @param accountUuid 指定账户的 UUID
     * @param roleUuid 指定角色的 UUID
     * @return ResponseBean
     */
    @PostMapping(value = "add")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object addAccountRole(@RequestParam("account_uuid") String accountUuid,
                          @RequestParam("role_uuid") String roleUuid) {
        return accountRolesMapService.addAccountRoleByUuid(accountUuid, roleUuid);
    }

    /**
     * 删除指定账户的一条角色
     * @param accountUuid 指定账户的 UUID
     * @param roleUuid 指定角色的 UUID
     * @return ResponseBean
     */
    @DeleteMapping(value = "delete")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object deleteAccountRole(@RequestParam("account_uuid") String accountUuid,
                             @RequestParam("role_uuid") String roleUuid) {
        return accountRolesMapService.deleteAccountRoleByUuid(accountUuid, roleUuid);
    }

    /**
     * 对指定账户重新设置角色
     * @param accountUuid accountUuid/accountName 二选一，指定账号的 UUID
     * @param accountName account_uuid/account_name 二选一，指定账号名
     * @param roleUuidList 英文逗号分隔，roleUuidList/roleNameList 二选一，指定要设置的角色列表，不在列表范围内的角色将被删除与账号的关联
     * @param roleNameList 英文逗号分隔，roleUuidList/roleNameList 二选一，指定要设置的角色列表，不在列表范围内的角色将被删除与账号的关联
     * @return ResponseBean
     */
    @PostMapping(value = "set")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object setAccountRoles(@RequestParam(value = "account_uuid", required = false) String accountUuid,
                           @RequestParam(value = "account_name", required = false) String accountName,
                           @RequestParam(value = "role_uuid_list", required = false) String roleUuidList,
                           @RequestParam(value = "role_name_list", required = false) String roleNameList) {
        return accountRolesMapService.setAccountRoles(accountUuid, accountName, roleUuidList, roleNameList);
    }
}
