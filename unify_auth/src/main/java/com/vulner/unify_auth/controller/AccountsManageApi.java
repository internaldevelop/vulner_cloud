package com.vulner.unify_auth.controller;

import com.google.common.base.Strings;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.bean.dto.AccountPersonalInfoDto;
import com.vulner.unify_auth.bean.dto.AccountRegisterDto;
import com.vulner.unify_auth.service.AccountsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 账号管理 API 接口
 */
@RestController
@RequestMapping(value = "/account_manage", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountsManageApi {
    @Autowired
    private AccountsManageService accountsManageService;

    /**
     * 读取所有账号
     * @return ResponseBean.payload: 包含所有账号数据的数组
     */
    @GetMapping(value = "/all", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object getAllAccounts() {
        return accountsManageService.getAllAccounts();
    }

    /**
     * 获得当前认证用户的账号信息记录
     * @param user 当前认证用户(access_token对应的用户）
     * @return ResponseBean.payload: 当前认证用户的账号信息记录
     */
    @GetMapping(value = "/self", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('current-user')")
    public @ResponseBody
    Object getSelfAccountInfo(Principal user) {
        if (user == null || Strings.isNullOrEmpty(user.getName())) {
            return ResponseHelper.error("ERROR_INVALID_ACCOUNT");
        }

        return accountsManageService.getAccountInfo(user.getName());
    }

    /**
     * 删除指定 UUID 的账户
     * @param accountUuid 账号 UUID
     * @return ResponseBean
     */
    @DeleteMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object deleteAccount(@RequestParam("account_uuid") String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }
        return accountsManageService.deleteAccountByUuid(accountUuid);
    }

    /**
     * 更新账号的个人信息（不含密码等隐私信息）
     * @param personalInfoDto 账号的个人信息
     * @param bindingResult
     * @return ResponseBean
     */
    @PostMapping(value = "/update", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object updateAccount(@Valid AccountPersonalInfoDto personalInfoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.invalidParams(bindingResult);
        }
        return accountsManageService.updateAccountPersonalInfo(personalInfoDto);
    }

    /**
     * 回收账号，设置账号为未激活状态
     * @param accountUuid 账号的 UUID
     * @return ResponseBean
     */
    @DeleteMapping(value = "/revoke", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object revokeAccount(@RequestParam("account_uuid") String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }
        return accountsManageService.revokeAccount(accountUuid);
    }

    /**
     * 激活账号，设置账号为激活状态
     * @param accountUuid  账号的 UUID
     * @return ResponseBean
     */
    @GetMapping(value = "/activate", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object activateAccount(@RequestParam("account_uuid") String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }
        return accountsManageService.activateAccount(accountUuid);
    }

    /**
     * 解锁密码，并清除尝试次数
     * @param accountUuid 账号的 UUID
     * @return ResponseBean
     */
    @GetMapping(value = "/unlock_pwd", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object unlockAccount(@RequestParam("account_uuid") String accountUuid) {
        if (Strings.isNullOrEmpty(accountUuid)) {
            return ResponseHelper.blankParams("账号 UUID ");
        }
        return accountsManageService.unlockAccountPassword(accountUuid);
    }

    @PostMapping(value = "/change_pwd", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object changePassword(Principal user,
                          @RequestParam("old_pwd") String oldPwd,
                          @RequestParam("new_pwd") String newPwd) {
        if (user == null || Strings.isNullOrEmpty(user.getName())) {
            return ResponseHelper.error("ERROR_INVALID_ACCOUNT");
        }
        return accountsManageService.changePassword(user.getName(), oldPwd, newPwd);
    }
}
