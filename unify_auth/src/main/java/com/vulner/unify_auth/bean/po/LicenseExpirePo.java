package com.vulner.unify_auth.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * License授权到期时间表
 */
@Component
@Data
public class LicenseExpirePo {
    private int id;
    private String uuid;
    private String issuer_uuid;
    private String issuer_name;
    private String account_uuid;
    private String account_name;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expire_time;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

}
