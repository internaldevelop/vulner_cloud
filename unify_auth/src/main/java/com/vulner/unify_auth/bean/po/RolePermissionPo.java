package com.vulner.unify_auth.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jason
 * @create 2020/1/2
 * @since 1.0.0
 * @description 角色权限映射表的 PO 类（和表结构一致）
 */
@Component
@Data
public class RolePermissionPo {
    private int id;
    private String uuid;
    private String role_uuid;
    private String permission_uuid;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
}
