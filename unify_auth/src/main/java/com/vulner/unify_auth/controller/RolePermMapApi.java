package com.vulner.unify_auth.controller;

import com.vulner.unify_auth.service.RolePermsMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( value = "role_perm_map", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolePermMapApi {
    @Autowired
    private RolePermsMapService rolePermsMapService;

    @GetMapping(value = "all")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object allRolePerms(@RequestParam("role_uuid") String roleUuid) {
        return rolePermsMapService.fetchAllPermsOfRole(roleUuid);
    }

    @PostMapping(value = "add")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object addRolePerm(@RequestParam("role_uuid") String roleUuid,
                       @RequestParam("perm_uuid") String permUuid) {
        return rolePermsMapService.addRolePermMapByUuid(roleUuid, permUuid);
    }

    @DeleteMapping(value = "delete")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object deleteRolePerm(@RequestParam("role_uuid") String roleUuid,
                          @RequestParam("perm_uuid") String permUuid) {
        return rolePermsMapService.deleteRolePermMapByUuid(roleUuid, permUuid);
    }

    @PostMapping(value = "set")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object setRolePerms(@RequestParam(value = "role_uuid", required = false) String roleUuid,
                        @RequestParam(value = "role_name", required = false) String roleName,
                        @RequestParam(value = "perm_uuid_list", required = false) String permUuidList,
                        @RequestParam(value = "perm_name_list", required = false) String permNameList) {
        return rolePermsMapService.setRolePerms(roleUuid, roleName, permUuidList, permNameList);
    }
}
