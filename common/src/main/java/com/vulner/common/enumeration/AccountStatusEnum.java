package com.vulner.common.enumeration;

public enum AccountStatusEnum {
    ACTIVE(0),
    DEACTIVE(1),
    LOCKED(2),
    ;

    private int status;

    AccountStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
