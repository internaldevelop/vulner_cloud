package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Jason
 * @create 2020/1/2
 * @since 1.0.0
 * @description 权限 DTO 类（只包含应用常用的权限信息）
 */
@Component
@Data
public class PermissionDto {
    private String permission_uuid;
    private String permission_name;
    private String permission_desc;
}
