package com.vulner.unify_auth.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Jason
 * @create 2020/1/2
 * @since 1.0.0
 * @description 角色权限映射的单条记录信息
 */
@Component
@Data
public class RolePermissionDto {
    private String role_uuid;
    private String role_name;
    private String role_alias;
    private List<PermissionDto> permissions;
}
