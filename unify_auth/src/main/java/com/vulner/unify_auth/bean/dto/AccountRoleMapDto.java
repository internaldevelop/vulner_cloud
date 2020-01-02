package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AccountRoleMapDto {
    private String account_uuid;
    private String account_name;
    private String account_alias;
    private String role_uuid;
    private String role_name;
    private String role_alias;
}
