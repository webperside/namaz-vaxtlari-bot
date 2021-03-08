package com.webperside.namazvaxtlaribot.telegram.enums;

import com.webperside.namazvaxtlaribot.telegram.exceptions.CommandNotFoundException;

public enum TelegramCommand {

    UNDEFINED("undefined"),
    START("start"),
    VAXTLAR("vaxtlar"),
    TENZIMLE("tenzimle"),
    HELP("help"),
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

    public static String getValue(int index) throws CommandNotFoundException{
        if(index < values().length && index >= 0){
            return values()[index].getCommand();
        }
        return values()[0].getValue();
    }
}
