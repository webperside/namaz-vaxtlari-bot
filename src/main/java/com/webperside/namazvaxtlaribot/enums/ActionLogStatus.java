package com.webperside.namazvaxtlaribot.enums;

public enum ActionLogStatus {

    FAILED(0),
    SUCCESS(1);

    private final byte value;

    ActionLogStatus(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
