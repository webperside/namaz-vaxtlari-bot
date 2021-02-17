package com.webperside.namazvaxtlaribot.telegram.exceptions;

public class CommandNotFoundException extends RuntimeException{

    private final String message;

    public CommandNotFoundException(String message) {
        this.message = message;
    }

}
