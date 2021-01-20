package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TelegramListener {

    private final TelegramService telegramService;

    public void listener(){
        TelegramConfig.getInstance().setUpdatesListener(updates -> {

            updates.forEach(update -> {
                try {
                    telegramService.process(update);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }


}
