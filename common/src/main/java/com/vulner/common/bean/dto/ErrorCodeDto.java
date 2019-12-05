package com.vulner.common.bean.dto;

import lombok.Data;

@Data
public class ErrorCodeDto {
    private int id;

    private String code;

    private String group;

    private String concept;

    private String description;
}
