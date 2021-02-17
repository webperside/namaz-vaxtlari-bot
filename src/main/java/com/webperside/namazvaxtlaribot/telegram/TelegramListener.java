package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.webperside.namazvaxtlaribot.service.MessageCreatorService;
import com.webperside.namazvaxtlaribot.telegram.enums.RequestType;
import com.webperside.namazvaxtlaribot.telegram.exceptions.CommandNotFoundException;
import com.webperside.namazvaxtlaribot.telegram.handlers.TelegramHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

import static com.webperside.namazvaxtlaribot.config.Constants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramListener {

    private final TelegramHelper helper;
    private final TelegramHandler handler;
    private final MessageCreatorService messageCreatorService;

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

                Long chatId = request.equals(RequestType.UPDATE) ?
                        update.message().chat().id() :
                        update.callbackQuery().message().chat().id();

                log.info("Working on Update ID : [{}], User ID : [{}], Chat ID : [{}], RequestType : [{}]", update.updateId(), userId, chatId, request.name());

                try {
                    handler.processRequest(update);
                } catch (CommandNotFoundException e){ // UI exception
                    printLogException(userId, chatId, e);

                    String customMessage = messageCreatorService.commandNotFoundCreator(update.message().text());
                    handler.handleException().run(update, update.callbackQuery(), customMessage);
                    return update.updateId();
                } catch (EntityNotFoundException e){ // UI exception
                    printLogException(userId, chatId, e);

                    String customMessage = messageCreatorService.exceptionMessageCreator(NOT_SPECIAL);
                    if(e.getMessage() != null && e.getMessage().equals(PRAY_TIME_SETTLEMENT_NOT_FOUND)){
                        customMessage = messageCreatorService.exceptionMessageCreator(PRAY_TIME_SETTLEMENT_NOT_FOUND);
                    }
                    handler.handleException().run(update, update.callbackQuery(), customMessage);
                    return update.updateId();
                } catch (Exception e) { // technical exception

                    e.printStackTrace();

                    printLogException(userId, chatId, e);

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

    private void printLogException(Integer userId, Long chatId, Exception e){
        log.error("User ID [{}], Chat ID [{}], Exception [{}], Message [{}]", userId, chatId, e.getClass().getSimpleName(), e.getMessage());
    }

}
