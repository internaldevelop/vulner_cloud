package com.vulner.unify_auth.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@Data
public class AccountRegisterDto {
    @NotNull(message = "账号不能为空")
    private String name;

    @NotNull(message = "姓名不能为空")
    private String alias;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "电子邮件不能为空")
    @Email(message = "邮件地址格式有误")
    private String email;

    @NotNull(message = "手机号码不能为空")
    private String mobile;

    @NotNull(message = "性别不能为空")
    private String gender;

    @NotNull(message = "出生日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
}
