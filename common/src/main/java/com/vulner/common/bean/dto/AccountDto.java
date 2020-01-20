package com.vulner.common.bean.dto;

import com.vulner.common.bean.po.AccountPo;
import lombok.Data;

@Data
public class AccountDto extends AccountPo {
    private String expire_flag;  // 过期标识 0:过期; 1:未过期
}
