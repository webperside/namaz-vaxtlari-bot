package com.webperside.namazvaxtlaribot.enums.telegram;

public enum TelegramCommand {

    START("start");

    private final String command;

    TelegramCommand(String command) {
        this.command = "/"+command;
    }

    public String getCommand() {
        return command;
    }
}
