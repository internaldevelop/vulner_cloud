package com.vulner.unify_auth.service;

import com.google.common.base.Strings;
import com.vulner.common.bean.po.PermissionPo;
import com.vulner.common.bean.po.RolePo;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.ObjUtils;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.unify_auth.dao.PermissionsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionsManageService {
    @Autowired
    private PermissionsDao permissionsDao;

    public ResponseBean fetchAllPermissions() {
        List<PermissionPo> permissionPos = permissionsDao.allPermissions();
        if (ObjUtils.nullOrEmptyList(permissionPos)) {
            return ResponseHelper.error("ERROR_NONE_PERMS");
        }

        return ResponseHelper.success(permissionPos);
    }

    public ResponseBean searchPermByUuid(String permUuid) {
        if (Strings.isNullOrEmpty(permUuid)) {
            return ResponseHelper.blankParams("权限 UUID ");
        }

        PermissionPo permissionPo = permissionsDao.serachByUuid(permUuid);
        if (permissionPo == null) {
            return ResponseHelper.error("ERROR_PERM_NOT_EXIST", permUuid);
        }
        return ResponseHelper.success(permissionPo);
    }

    public ResponseBean addPerm(String permName, String permDesc) {
        if (Strings.isNullOrEmpty(permName)) {
            return ResponseHelper.blankParams("权限名称");
        }

        if (permDesc == null) {
            permDesc = "";
        }

        // 构建 PO 对象
        PermissionPo permissionPo = new PermissionPo();
        permissionPo.setUuid(StringUtils.generateUuid());
        permissionPo.setName(permName);
        permissionPo.setDescription(permDesc);
        permissionPo.setCreate_time(TimeUtils.getCurrentDate());
        permissionPo.setUpdate_time(TimeUtils.getCurrentDate());

        // 添加权限记录
        int row = permissionsDao.addPermission(permissionPo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_CREATE_FAILED");
        }
        return ResponseHelper.success(permissionPo);
    }

    public ResponseBean deletePermByUuid(String permUuid) {
        if (Strings.isNullOrEmpty(permUuid)) {
            return ResponseHelper.blankParams("权限 UUID ");
        }

        // 检查是否存在要删除的权限
        PermissionPo permissionPo = permissionsDao.serachByUuid(permUuid);
        if (permissionPo == null) {
            return ResponseHelper.error("ERROR_PERM_NOT_EXIST", permUuid);
        }

        // 删除权限
        int row = permissionsDao.deletePermission(permUuid);
        if (row != 1) {
            return ResponseHelper.error("ERROR_REMOVE_FAILED");
        }
        return ResponseHelper.success();
    }

    public ResponseBean updatePerm(String permUuid, String permName, String permDesc) {
        // 权限 UUID 和权限名称是必须提供的参数
        if (Strings.isNullOrEmpty(permUuid)) {
            return ResponseHelper.blankParams("权限 UUID ");
        }
        if (Strings.isNullOrEmpty(permName)) {
            return ResponseHelper.blankParams("权限名称");
        }

        // 检查是否存在要更新的权限
        PermissionPo permissionPo = permissionsDao.serachByUuid(permUuid);
        if (permissionPo == null) {
            return ResponseHelper.error("ERROR_PERM_NOT_EXIST", permUuid);
        }

        // 构建 PO 对象
        permissionPo.setName(permName);
        // 权限描述未提供，则不更新权限的描述内容
        if (!Strings.isNullOrEmpty(permDesc)) {
            permissionPo.setDescription(permDesc);
        }
        permissionPo.setUpdate_time(TimeUtils.getCurrentDate());

        // 更新权限信息
        int row = permissionsDao.updatePermission(permissionPo);
        if (row != 1) {
            return ResponseHelper.error("ERROR_UPDATE_FAILED");
        }
        return ResponseHelper.success();
    }

//    public ResponseBean getPermUuid(String permName) {
//    }
}
