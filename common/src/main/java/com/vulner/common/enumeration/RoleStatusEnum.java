package com.vulner.common.enumeration;

public enum RoleStatusEnum {
    INVALID(0),
    VALID(1),
            ;

    private int status;

    RoleStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
