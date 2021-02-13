package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.webperside.namazvaxtlaribot.config.Constants.ADMIN_TELEGRAM_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramListener {

    public enum RequestType{
        UPDATE,
        CALLBACK;

        public static RequestType determineRequest(Update update){
            if(update.message() != null) return UPDATE;
            else if(update.message() == null && update.callbackQuery() != null) return CALLBACK;
            return UPDATE;
        }
    }

    private final TelegramService telegramService;

    public void listener() {
        TelegramConfig.getInstance().setUpdatesListener(updates -> {

            log.info("Updates size : [{}]", updates.size());

            for (Update update : updates) {
                RequestType request = RequestType.determineRequest(update);

                long userId = request.equals(RequestType.UPDATE) ?
                        update.message().from().id() :
                        update.callbackQuery().message().from().id();

                log.info("Working on Update ID : [{}]", update.updateId());
                TelegramConfig.execute(new SendMessage(ADMIN_TELEGRAM_ID, userId + " new user ID"));

                try {
                    telegramService.process(update);
                } catch (Exception e) {

                    final String ERROR_MESSAGE_TEMPLATE = "USER ID - %s\n" +
                            "EXCEPTION - %s\n" +
                            "MESSAGE - %s";

                    log.error("User ID [{}], Exception [{}], Message [{}]", userId, e.getClass().getSimpleName(), e.getMessage());

                    TelegramConfig.execute(new SendMessage(ADMIN_TELEGRAM_ID,
                                    String.format(
                                            ERROR_MESSAGE_TEMPLATE,
                                            userId,
                                            e.getClass().getSimpleName(),
                                            e.getMessage())
                            )
                    );
                    return update.updateId();
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

}
