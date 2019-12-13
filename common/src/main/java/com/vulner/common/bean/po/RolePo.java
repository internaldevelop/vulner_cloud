package com.vulner.common.bean.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class RolePo {
    private int id;
    private String uuid;
    private String name;
    private short valid;
    private Date create_time;
    private Date update_time;
}
