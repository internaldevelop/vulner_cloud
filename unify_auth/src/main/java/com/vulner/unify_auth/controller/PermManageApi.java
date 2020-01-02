package com.vulner.unify_auth.controller;

import com.vulner.unify_auth.service.PermissionsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jason
 * @create 2020/1/2
 * @since 1.0.0
 * @description 权限管理 API 接口
 */
@RestController
@RequestMapping(value = "/perm_manage", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermManageApi {
    @Autowired
    private PermissionsManageService permManageService;

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object allPermissions() {
        return permManageService.fetchAllPermissions();
    }

    @GetMapping(value = "/perm_info")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object serachPermByUuid(@RequestParam("perm_uuid") String permUuid) {
        return permManageService.searchPermByUuid(permUuid);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object addPermission(@RequestParam("name") String permName,
                         @RequestParam("description") String permDesc) {
        return permManageService.addPerm(permName, permDesc);
    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object deletePermission(@RequestParam("perm_uuid") String permUuid) {
        return permManageService.deletePermByUuid(permUuid);
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object updatePermission(@RequestParam("perm_uuid") String permUuid,
                            @RequestParam("name") String permName,
                            @RequestParam("description") String permDesc) {
        return permManageService.updatePerm(permUuid, permName, permDesc);
    }

    @GetMapping(value = "/perm_uuid")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object getPermUuid(@RequestParam("name") String permName) {
        return permManageService.getPermUuid(permName);
    }

}
