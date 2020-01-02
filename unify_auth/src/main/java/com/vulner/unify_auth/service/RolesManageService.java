package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.po.RolePo;
import com.vulner.common.enumeration.RoleStatusEnum;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.ObjUtils;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.dao.RolesDao;
import com.vulner.unify_auth.service.helper.RolesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RolesManageService {
    @Autowired
    private RolesDao rolesDao;

    @Autowired
    private RolePermsMapService rolePermsMapService;

    @Autowired
    private AccountRolesMapService accountRolesMapService;

    public ResponseBean fetchAllRoles() {
        List<RolePo> rolePoList = rolesDao.fetchAllRoles();
        if (ObjUtils.nullOrEmptyList(rolePoList)) {
            return ResponseHelper.error("ERROR_NONE_ROLES");
        }

        return ResponseHelper.success(rolePoList);
    }

    public ResponseBean searchRoleByUuid(String roleUuid) {
        if (Strings.isNullOrEmpty(roleUuid)) {
            return ResponseHelper.blankParams("角色 UUID ");
        }

        RolePo rolePo = rolesDao.searchByUuid(roleUuid);
        if (rolePo == null) {
            return ResponseHelper.error("ERROR_ROLE_NOT_EXIST");
        }

        return ResponseHelper.success(rolePo);
    }

    private ResponseBean _preCheck(String roleUuid, String roleName, String roleAlias) {
        // 角色名或角色别名，均不能为空
        if (Strings.isNullOrEmpty(roleName)) {
            return ResponseHelper.blankParams("角色名");
        }
        if (Strings.isNullOrEmpty(roleAlias)) {
            return ResponseHelper.blankParams("角色别名");
        }

        // 检查角色名的有效性
        if (!RolesHelper.validRoleName(roleName)) {
            return ResponseHelper.error("ERROR_INVALID_ROLE_NAME");
        }

        // 检查是否有重名
        List<RolePo> exist = null;
        if (Strings.isNullOrEmpty(roleUuid)) {
            exist = rolesDao.existNameOrAlias(roleName, roleAlias);
        } else {
            exist = rolesDao.existOtherNameOrAlias(roleUuid, roleName, roleAlias);
        }
        if (!ObjUtils.nullOrEmptyList(exist)) {
            return ResponseHelper.error("ERROR_ROLE_EXIST", "请检查角色名称或别名是否已存在或重名！");
        }

        return ResponseHelper.success();
    }

    public ResponseBean addRole(String roleName, String roleAlias) {
        // 对 roleName 和 roleAlias 检验有效性和唯一性
        ResponseBean responseBean = _preCheck("", roleName, roleAlias);
        if (!ResponseHelper.isSuccess(responseBean)) {
            return responseBean;
        }

        // 构建 PO 对象
        RolePo rolePo = new RolePo();
        rolePo.setUuid(StringUtils.generateUuid());
        rolePo.setName(roleName);
        rolePo.setAlias(roleAlias);
        rolePo.setValid((short) RoleStatusEnum.VALID.getStatus());
        rolePo.setCreate_time(TimeUtils.getCurrentDate());
        rolePo.setUpdate_time(TimeUtils.getCurrentDate());

        // 添加角色记录
        int row = rolesDao.addRole(rolePo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_CREATE_FAILED");
        }

        return ResponseHelper.success(rolePo);
    }

    public ResponseBean deleteRoleByUuid(String roleUuid) {
        if (Strings.isNullOrEmpty(roleUuid)) {
            return ResponseHelper.blankParams("角色 UUID ");
        }

        // 检查是否存在要删除的角色
        RolePo rolePo = rolesDao.searchByUuid(roleUuid);
        if (rolePo == null) {
            return ResponseHelper.error("ERROR_ROLE_NOT_EXIST", roleUuid);
        }

        // 删除角色
        int row = rolesDao.deleteRole(roleUuid);
        if (row != 1) {
            return ResponseHelper.error("ERROR_REMOVE_FAILED");
        }

        // 删除角色权限映射表中所有含该角色的项
        boolean rv = rolePermsMapService.deleteAllMapsByRoleUuid(roleUuid);
        if (!rv) {
            return ResponseHelper.error("ERROR_REMOVE_FAILED");
        }

        // 删除账号角色映射表中所有含该角色的项
        rv = accountRolesMapService.deleteAllMapsByRoleUuid(roleUuid);
        return ResponseHelper.success();
    }

    public ResponseBean updateRole(String roleUuid, String roleName, String roleAlias) {
        if (Strings.isNullOrEmpty(roleUuid)) {
            return ResponseHelper.blankParams("角色 UUID ");
        }

        // 对 roleName 和 roleAlias 检验有效性和唯一性
        ResponseBean responseBean = _preCheck(roleUuid, roleName, roleAlias);
        if (!ResponseHelper.isSuccess(responseBean)) {
            return responseBean;
        }

        // 检查是否存在要更新的角色
        RolePo rolePo = rolesDao.searchByUuid(roleUuid);
        if (rolePo == null) {
            return ResponseHelper.error("ERROR_ROLE_NOT_EXIST", roleUuid);
        }

        // 构建 PO 对象
        rolePo.setName(roleName);
        rolePo.setAlias(roleAlias);
        rolePo.setUpdate_time(TimeUtils.getCurrentDate());

        // 更新角色信息
        int row = rolesDao.updateRoleName(rolePo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_UPDATE_FAILED");
        }
        return ResponseHelper.success();
    }

    public ResponseBean getRoleUuid(String roleName, String roleAlias) {
        String roleUuid;
        if (!Strings.isNullOrEmpty(roleName)) {
            // 如果给定角色名，则用角色名转 UUID
            roleUuid = rolesDao.getRoleUuidByName(roleName);
        } else if (!Strings.isNullOrEmpty(roleAlias)) {
            // 没有给定角色名，但给定了角色别名，则用别名转 UUID
            roleUuid = rolesDao.getRoleUuidByAlias(roleAlias);
        } else {
            return ResponseHelper.error("ERROR_PARAMS_MISSING", "必须输入角色名或角色别名！");
        }

        if (Strings.isNullOrEmpty(roleUuid)) {
            return ResponseHelper.error("ERROR_ROLE_NOT_EXIST");
        }

        return ResponseHelper.success(roleUuid);
    }
}
