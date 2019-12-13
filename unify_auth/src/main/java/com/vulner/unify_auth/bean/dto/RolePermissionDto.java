package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RolePermissionDto {
    private String role_uuid;
    private String permission_uuid;
    private String permission_method;
    private String gateway_prefix;
    private String service_prefix;
    private String permission_uri;
}
