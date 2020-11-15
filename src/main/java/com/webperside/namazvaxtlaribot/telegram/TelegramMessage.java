package com.webperside.namazvaxtlaribot.telegram;

public enum TelegramMessage {
    START_MESSAGE("Salam, %s xoş gəldin! :)");

    private final String message;

    TelegramMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
