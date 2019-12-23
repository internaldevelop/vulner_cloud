package com.vulner.unify_auth.bean.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PasswdParamsDto {
    private String account_uuid;
    private int max_attempts;
    private int attempts;
    private short locked;
}
