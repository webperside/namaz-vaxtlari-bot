package com.webperside.namazvaxtlaribot.enums;

public enum UserStatus {
    STOPPED(0),
    ACTIVE(1);

    private final byte value;

    UserStatus(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
