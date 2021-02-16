package com.webperside.namazvaxtlaribot.telegram.exceptions;

import lombok.Data;

public class CommandNotFoundException extends RuntimeException{

    private final String message;

    public CommandNotFoundException(String message) {
        this.message = message;
    }

}
