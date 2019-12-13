package com.vulner.unify_auth.dao;

import com.vulner.unify_auth.bean.dto.RolePermissionDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RolePermissionsDao {
    @Select("SELECT\n" +
            "\trp.role_uuid,\n" +
            "\trp.permission_uuid,\n" +
            "\tp.method AS permission_method,\n" +
            "\tp.gateway_prefix,\n" +
            "\tp.service_prefix,\n" +
            "\tp.uri AS permission_uri\n" +
            "FROM role_permissions rp\n" +
            "LEFT JOIN permissions p ON rp.permission_uuid=p.uuid\n" +
            "WHERE rp.role_uuid=#{roleUuid};")
    List<RolePermissionDto> getPermissions(String roleUuid);
}
