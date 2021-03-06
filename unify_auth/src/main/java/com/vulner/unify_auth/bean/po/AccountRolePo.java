package com.vulner.unify_auth.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jason
 * @create 2019/12/21
 * @since 1.0.0
 * @description 账号角色映射表的 PO 类（和表结构一致）
 */
@Component
@Data
public class AccountRolePo {
    private int id;
    private String uuid;
    private String account_uuid;
    private String role_uuid;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
}
