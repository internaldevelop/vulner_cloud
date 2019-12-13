package com.vulner.common.bean.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class PermissionPo {
    private int id;
    private String uuid;
    private String method;
    private String gateway_prefix;
    private String service_prefix;
    private String uri;
    private Date create_time;
    private Date update_time;
}
