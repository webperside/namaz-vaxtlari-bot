package com.webperside.namazvaxtlaribot.enums;

public enum NotifyMeStatus {
    DISABLED(0),
    ENABLED(1);

    private final byte value;

    NotifyMeStatus(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
