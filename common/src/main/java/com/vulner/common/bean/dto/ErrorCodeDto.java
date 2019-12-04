package com.vulner.common.bean.dto;

import lombok.Data;

@Data
public class ErrorCodeDto {
    private int id;

    private String name;

    private String group;

    private String concept;

    private String description;
}
