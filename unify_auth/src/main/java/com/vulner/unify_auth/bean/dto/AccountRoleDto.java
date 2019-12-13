package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AccountRoleDto {
    private String account_uuid;
    private String role_uuid;
    private String role_name;
}
