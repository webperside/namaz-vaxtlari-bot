package com.webperside.namazvaxtlaribot.telegram.enums;

import com.pengrad.telegrambot.model.Update;

public enum RequestType{
    UPDATE,
    CALLBACK;

    public static RequestType determineRequest(Update update){
        if(update.message() != null) return UPDATE;
        else if(update.message() == null && update.callbackQuery() != null) return CALLBACK;
        return null;
    }
}