package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.TelegramBot;

import java.util.Base64;

public class TelegramConfig {

    private static final String TOKEN = "MTQ0NDkwMjEzNjpBQUZxaF90Z3dvMl8zdG5MbGFxSXh3TjZGYlBKemVmbnFLVQ==";
    private static TelegramBot bot;

    public static TelegramBot getInstance(){
        if(bot != null) return bot;

        bot = new TelegramBot(new String(Base64.getDecoder().decode(TOKEN)));
        return bot;
    }

}
