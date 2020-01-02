package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jason
 * @create 2019/12/21
 * @since 1.0.0
 * @description 账号角色映射的单条记录信息
 */
@Component
@Data
public class AccountRolesDto {
    private String account_uuid;
    private String account_name;
    private String account_alias;
    private List<RoleDto> roles;
}
