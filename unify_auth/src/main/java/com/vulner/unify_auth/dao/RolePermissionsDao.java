package com.vulner.unify_auth.dao;

import com.vulner.unify_auth.bean.dto.RolePermissionDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jason
 * @create 2019/12/26
 * @since 1.0.0
 * @description 角色权限映射表的操作（Mybatis）
 */

@Component
public interface RolePermissionsDao {
    @Select("SELECT\n" +
            "\trp.role_uuid,\n" +
            "\trp.permission_uuid,\n" +
            "\tp.name AS permission_name,\n" +
            "\tp.description AS permission_desc,\n" +
            "\tp.create_time,\n" +
            "\tp.update_time\n" +
            "FROM role_permissions rp\n" +
            "LEFT JOIN permissions p ON rp.permission_uuid=p.uuid\n" +
            "WHERE rp.role_uuid=#{roleUuid};")
    List<RolePermissionDto> getPermissions(String roleUuid);
}
