package com.vulner.system_code.global;

import lombok.Data;

@Data
public class ErrorCodeEntity {
    private int id;

    private String name;

    private String group;

    private String concept;

    private String description;
}
