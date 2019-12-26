package com.vulner.common.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
}
