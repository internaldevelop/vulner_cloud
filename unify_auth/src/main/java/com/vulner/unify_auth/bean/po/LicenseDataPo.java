package com.vulner.unify_auth.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * License授权表
 */
@Component
@Data
public class LicenseDataPo {
    private int id;
    private String uuid;
    private String issuer_uuid;
    private String issuer_name;
    private String license_data;
    private int use_flag;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date use_time;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

}
