package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RolePermMapDto {
    private String role_uuid;
    private String role_name;
    private String role_alias;
    private String permission_uuid;
    private String permission_name;
    private String permission_desc;
}
