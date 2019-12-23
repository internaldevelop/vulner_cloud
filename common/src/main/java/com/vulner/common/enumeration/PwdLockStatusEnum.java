package com.vulner.common.enumeration;

public enum PwdLockStatusEnum {
    UNLOCKED((short)0),
    LOCKED((short)1),
    ;

    private short locked;

    PwdLockStatusEnum(short locked) {
        this.locked = locked;
    }

    public short getLocked() {
        return locked;
    }

    public void setLocked(short locked) {
        this.locked = locked;
    }
}
