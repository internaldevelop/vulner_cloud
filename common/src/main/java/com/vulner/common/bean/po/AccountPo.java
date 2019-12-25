package com.vulner.common.bean.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class AccountPo {
    private int id;
    private String uuid;
    private String name;
    private String alias;
    private String password;
    private String salt;
    private int max_attempts;
    private int attempts;
    private short locked;
    private int status;
    private String email;
    private String mobile;
    private String gender;
    private Date birthday;
    private Date create_time;
}
