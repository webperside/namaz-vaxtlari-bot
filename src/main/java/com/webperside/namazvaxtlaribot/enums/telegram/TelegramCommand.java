package com.webperside.namazvaxtlaribot.enums.telegram;

import com.webperside.namazvaxtlaribot.telegram.exceptions.CommandNotFoundException;

import java.util.Locale;

public enum TelegramCommand {

    START("start"),
    VAXTLAR("vaxtlar"),
    TEST("test");

    private final String command;
    private final String value;

    TelegramCommand(String command) {
        this.command = "/"+command;
        this.value = command;
    }

    public String getCommand() {
        return command;
    }

    public String getValue(){ return value; }

    public static String getValue(String command) throws CommandNotFoundException {
        for(TelegramCommand telegramCommand : values()){
            if(telegramCommand.getCommand().equals(command.toLowerCase())){
                return telegramCommand.getValue();
            }
        }
        throw new CommandNotFoundException(String.format("%s command not found",command));
    }
}
