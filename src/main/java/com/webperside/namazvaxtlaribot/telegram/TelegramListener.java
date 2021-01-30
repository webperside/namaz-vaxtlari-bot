package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TelegramListener {

    private final TelegramService telegramService;

    public void listener(){
        TelegramConfig.getInstance().setUpdatesListener(updates -> {

            for (Update update : updates) {
                try {
                    telegramService.process(update);
                } catch (IOException e) {
                    e.printStackTrace();
                    return update.updateId();
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }


}
