package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramListener {

    private final TelegramService telegramService;

    public void listener(){
        TelegramConfig.getInstance().setUpdatesListener(updates -> {
            updates.forEach(telegramService::process);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }


}
