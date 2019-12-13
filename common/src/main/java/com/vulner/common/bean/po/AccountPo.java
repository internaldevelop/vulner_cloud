package com.vulner.common.bean.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class AccountPo {
    private int id;
    private String uuid;
    private String account;
    private String name;
    private String password;
    private String email;
    private String mobile;
    private short sex;
    private Date birthday;
    private Date create_time;
}
