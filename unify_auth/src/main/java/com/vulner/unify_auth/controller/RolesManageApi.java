package com.vulner.unify_auth.controller;

import com.google.common.base.Strings;
import com.vulner.common.response.ResponseHelper;
import com.vulner.unify_auth.dao.RolesDao;
import com.vulner.unify_auth.service.RolesManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 角色管理 API 接口，以及账号的角色关联和解除
 */
@RestController
@RequestMapping(value = "/role_manage", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolesManageApi {
    @Autowired
    RolesManageService rolesManageService;

    /**
     * 读取所有的角色，并返回记录列表
     * @return 所有角色记录的列表
     */
    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object allRoles() {
        return rolesManageService.fetchAllRoles();
    }

    /**
     * 读取指定 UUID 的角色记录
     * @param roleUuid 角色 UUID
     * @return 指定的角色记录
     */
    @GetMapping(value = "/role_info")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object searchRoleByUuid(@RequestParam("role_uuid") String roleUuid) {
        return rolesManageService.searchRoleByUuid(roleUuid);
    }

    /**
     * 增加角色记录，不允许角色名和化名重复，需遵守全局唯一性
     * @param roleName 角色名
     * @param roleAlias 角色化名
     * @return ResponseBean.payload: 新增的角色记录
     */
    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object addRole(@RequestParam("role_name") String roleName,
                   @RequestParam("role_alias") String roleAlias) {
        return rolesManageService.addRole(roleName, roleAlias);
    }

    /**
     * 删除指定的角色记录
     * @param roleUuid 指定角色的 UUID
     * @return ResponseBean
     */
    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object deleteRole(@RequestParam("role_uuid") String roleUuid) {
        return rolesManageService.deleteRoleByUuid(roleUuid);
    }

    /**
     * 更新指定的角色信息
     * @param roleUuid 指定角色的 UUID
     * @param roleName 角色名称
     * @param roleAlias 角色化名
     * @return ResponseBean
     */
    @PostMapping(value = "/update")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object updateRole(@RequestParam("role_uuid") String roleUuid,
                      @RequestParam("role_name") String roleName,
                      @RequestParam("role_alias") String roleAlias) {
        return rolesManageService.updateRole(roleUuid, roleName, roleAlias);
    }

    /**
     * 获取指定的角色名称或别名的角色 UUID
     * 角色名称或角色别名 二选一
     * @param roleName
     * @param roleAlias
     * @return ResponseBean.payload: 角色 UUID
     */
    @GetMapping(value = "/role_uuid")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public @ResponseBody
    Object getRoleUuid(@RequestParam(value = "role_name", required = false) String roleName,
                       @RequestParam(value = "role_alias", required = false) String roleAlias) {
        return rolesManageService.getRoleUuid(roleName, roleAlias);
    }

}
