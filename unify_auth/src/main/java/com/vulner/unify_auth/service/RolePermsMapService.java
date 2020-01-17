package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.po.RolePo;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.bean.dto.PermissionDto;
import com.vulner.unify_auth.bean.dto.RolePermMapDto;
import com.vulner.unify_auth.bean.dto.RolePermissionDto;
import com.vulner.unify_auth.bean.po.RolePermissionPo;
import com.vulner.unify_auth.dao.PermissionsDao;
import com.vulner.unify_auth.dao.RolePermissionsDao;
import com.vulner.unify_auth.dao.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 权限表的操作（Mybatis）
 */
@Component
public class RolePermsMapService {
    @Autowired
    private RolePermissionsDao rolePermsDao;

    @Autowired
    private RolesDao rolesDao;

    @Autowired
    private PermissionsDao permsDao;

    private ResponseBean _preCheck(String roleUuid, String permUuid) {
        if (Strings.isNullOrEmpty(roleUuid)) {
            return ResponseHelper.blankParams("角色 UUID ");
        }
        if (Strings.isNullOrEmpty(permUuid)) {
            return ResponseHelper.blankParams("权限 UUID ");
        }
        return ResponseHelper.success();
    }

    public ResponseBean fetchAllPermsOfRole(String roleUuid) {
        if (Strings.isNullOrEmpty(roleUuid)) {
            return ResponseHelper.blankParams("角色 UUID ");
        }

        // 查找角色信息
        RolePo rolePo = rolesDao.searchByUuid(roleUuid);
        if (rolePo == null) {
            return ResponseHelper.error("ERROR_ROLE_NOT_EXIST", roleUuid);
        }

        // 读取角色关联的所有权限信息
        List<PermissionDto> permissions = rolePermsDao.getPermissions(roleUuid);
        if (permissions == null) {
            // 如果 list 非法，则创建一个空的list
            permissions = new ArrayList<>();
        }

        // 构造响应数据（角色信息 + 权限列表）
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.setRole_uuid(roleUuid);
        rolePermissionDto.setRole_name(rolePo.getName());
        rolePermissionDto.setRole_alias(rolePo.getAlias());
        rolePermissionDto.setPermissions(permissions);

        // 成功返回角色关联的权限列表，或空列表
        return ResponseHelper.success(rolePermissionDto);
    }

    public ResponseBean addRolePermMapByUuid(String roleUuid, String permUuid) {
        // 检查参数
        ResponseBean responseBean = _preCheck(roleUuid, permUuid);
        if (!ResponseHelper.isSuccess(responseBean)) {
            return responseBean;
        }

        // 检查是否已存在角色权限对应关系
        RolePermMapDto rolePermMapDto = rolePermsDao.getRolePermMap(roleUuid, permUuid);
        if (rolePermMapDto != null) {
            return ResponseHelper.error("ERROR_ADD_ROLE_PERM_FAILED", rolePermMapDto);
        }

        // 准备角色权限映射对象
        RolePermissionPo rolePermissionPo = new RolePermissionPo();
        rolePermissionPo.setUuid(StringUtils.generateUuid());
        rolePermissionPo.setRole_uuid(roleUuid);
        rolePermissionPo.setPermission_uuid(permUuid);
        rolePermissionPo.setCreate_time(TimeUtils.getCurrentDate());

        // 添加一条角色权限映射记录
        int row = rolePermsDao.addRolePermMap(rolePermissionPo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_ADD_ROLE_PERM_FAILED");
        }

        return ResponseHelper.success(rolePermissionPo);
    }

    public ResponseBean deleteRolePermMapByUuid(String roleUuid, String permUuid) {
        // 检查参数
        ResponseBean responseBean = _preCheck(roleUuid, permUuid);
        if (!ResponseHelper.isSuccess(responseBean)) {
            return responseBean;
        }

        // 检查是否存在角色权限对应关系
        RolePermMapDto rolePermMapDto = rolePermsDao.getRolePermMap(roleUuid, permUuid);
        if (rolePermMapDto == null) {
            return ResponseHelper.error("ERROR_DEL_ROLE_PERM_FAILED");
        }

        // 删除该角色权限对应关系
        int row = rolePermsDao.deleteRolePermMap(roleUuid, permUuid);
        if (row != 1) {
            return ResponseHelper.error("ERROR_DEL_ROLE_PERM_FAILED");
        }

        return ResponseHelper.success();
    }

    public boolean deleteAllMapsByRoleUuid(String roleUuid) {
        if (Strings.isNullOrEmpty(roleUuid)) {
            return false;
        }

        int rows = rolePermsDao.deleteAllMapsByRoleUuid(roleUuid);
        return (rows > 0);
    }

    public boolean deleteAllMapsByPermUuid(String permUuid) {
        if (Strings.isNullOrEmpty(permUuid)) {
            return false;
        }

        int rows = rolePermsDao.deleteAllMapsByPermUuid(permUuid);
        return (rows > 0);
    }

    public ResponseBean setRolePerms(String roleUuid, String roleName, String permUuids, String permNames) {
        if (Strings.isNullOrEmpty(roleUuid) && Strings.isNullOrEmpty(roleName)) {
            return ResponseHelper.blankParams("角色 UUID 或角色名称");
        }
        if (Strings.isNullOrEmpty(permUuids) && Strings.isNullOrEmpty(permNames)) {
            return ResponseHelper.blankParams("权限 UUID 列表或权限名称列表");
        }

        // 解析权限列表
        List<String> permUuidList = new ArrayList<>();
        if (Strings.isNullOrEmpty(permUuids)) {
            String[] permNameList = permNames.split(",");
            for (String permName : permNameList) {
                String permUuid = permsDao.getPermUuidByName(permName);
                if (!Strings.isNullOrEmpty(permUuid)) {
                    permUuidList.add(permUuid);
                }
            }
        } else {
            String[] uuidList = permUuids.split(",");
            for (String uuid : uuidList) {
                int count = permsDao.getCountByUuid(uuid);
                if (count > 0) {
                    permUuidList.add(uuid);
                }
            }
        }

        // 删除指定角色的所有权限关联记录
        if (Strings.isNullOrEmpty(roleUuid)) {
            roleUuid = rolesDao.getRoleUuidByName(roleName);
        }
        rolePermsDao.deleteAllMapsByRoleUuid(roleUuid);

        // 增加指定账户的角色关联记录
        for (String permUuid : permUuidList) {
            addRolePermMapByUuid(roleUuid, permUuid);
        }

        // 成功返回
        return ResponseHelper.success();
    }
}