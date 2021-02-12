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
//                TelegramConfig.execute(new SendMessage("625929111",String.valueOf(update.message().from().id())));
//                TelegramConfig.execute(new SendMessage(update.message().from().id(),"Istifadeniz uchun teshekkurler !!!"));
                try {
                    telegramService.process(update);
                } catch (IOException e) {
                    System.out.println(update);
                    e.printStackTrace();
                    return update.updateId();
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }


}
