package com.vulner.common.enumeration;

public enum LogTypeEnum {
    SUCCESS(1),
    FAIL(2),
    SYS_ERROR(3),
    INFO(4),
    EXCEPT(5),
    WARNING(6),
    ;

    private int type;

    LogTypeEnum(int type) { this.type = type; }

    public int getType() { return this.type; }

    public void setType(int type) { this.type = type; }

}
