package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

import java.util.Base64;

public class TelegramConfig {

    private static final String TOKEN = "MTQ0NDkwMjEzNjpBQUZxaF90Z3dvMl8zdG5MbGFxSXh3TjZGYlBKemVmbnFLVQ==";
    private static TelegramBot bot;

    public static TelegramBot getInstance(){
        if(bot != null) return bot;

        bot = new TelegramBot(new String(Base64.getDecoder().decode(TOKEN)));
        return bot;
    }

    public static <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(T request){
        return getInstance().execute(request);
    }

}
