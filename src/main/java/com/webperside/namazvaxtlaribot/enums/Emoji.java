package com.webperside.namazvaxtlaribot.enums;

public enum Emoji {
    LEFT_ARROW("Left arrow","\u2B05", "left_a"),
    RIGHT_ARROW("Right arrow","\u27A1","right_a"),
    CHECK("Check","\u2705","check");

    private final String name;
    private final String value;
    private final String callback;

    Emoji(String name, String value, String callback) {
        this.name = name;
        this.value = value;
        this.callback = callback;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getCallback() {
        return callback;
    }
}
