package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.webperside.namazvaxtlaribot.telegram.enums.RequestType;
import com.webperside.namazvaxtlaribot.telegram.handlers.TelegramHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.webperside.namazvaxtlaribot.config.Constants.ADMIN_TELEGRAM_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramListener {

    private final TelegramHelper helper;
    private final TelegramHandler handler;

    private static final String ERROR_MESSAGE_TEMPLATE = "USER ID - %s\n" +
            "EXCEPTION - %s\n" +
            "MESSAGE - %s";

    public void listen() {
        helper.bot().setUpdatesListener(updates -> {

            log.info("Updates size : [{}]", updates.size());

            for (Update update : updates) {
                RequestType request = RequestType.determineRequest(update);

                Integer userId = request.equals(RequestType.UPDATE) ?
                        update.message().from().id() :
                        update.callbackQuery().from().id();

                log.info("Working on Update ID : [{}], User ID : [{}], RequestType : [{}]", update.updateId(), userId, request.name());

                try {
                    handler.processRequest(update);
                } catch (Exception e) {

                    e.printStackTrace();

                    log.error("User ID [{}], Exception [{}], Message [{}]", userId, e.getClass().getSimpleName(), e.getMessage());

                    helper.executor().sendText(
                            ADMIN_TELEGRAM_ID,
                            String.format(
                                    ERROR_MESSAGE_TEMPLATE,
                                    userId,
                                    e.getClass().getSimpleName(),
                                    e.getMessage()
                            )
                    );
                    return update.updateId();
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

}
