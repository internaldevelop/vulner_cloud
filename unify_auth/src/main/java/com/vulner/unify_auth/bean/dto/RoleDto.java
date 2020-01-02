package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Jason
 * @create 2020/1/2
 * @since 1.0.0
 * @description 角色 DTO 类（只包含应用常用的角色信息）
 */
@Component
@Data
public class RoleDto {
    private String role_uuid;
    private String role_name;
    private String role_alias;
}
