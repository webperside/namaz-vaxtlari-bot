package com.webperside.namazvaxtlaribot.telegram.exceptions;

public class CommandNotFoundException extends RuntimeException{

    public CommandNotFoundException(String message) {
        super(message);
    }

}
