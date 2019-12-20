package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class RolePermissionDto {
    private String role_uuid;
    private String permission_uuid;
    private String permission_name;
    private String permission_desc;
    private Date create_time;
    private Date update_time;
}
