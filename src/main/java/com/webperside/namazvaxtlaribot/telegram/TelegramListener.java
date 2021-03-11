package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.service.ActionLogService;
import com.webperside.namazvaxtlaribot.service.MessageCreatorService;
import com.webperside.namazvaxtlaribot.telegram.enums.RequestType;
import com.webperside.namazvaxtlaribot.telegram.enums.TelegramCommand;
import com.webperside.namazvaxtlaribot.telegram.exceptions.CommandNotFoundException;
import com.webperside.namazvaxtlaribot.telegram.handlers.TelegramHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

import java.util.Arrays;

import static com.webperside.namazvaxtlaribot.config.Constants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramListener {

    private final TelegramHelper helper;
    private final TelegramHandler handler;
    private final MessageCreatorService messageCreatorService;
    private final ActionLogService actionLogService;

    private static final String ERROR_MESSAGE_TEMPLATE = "USER ID - %s\n" +
            "EXCEPTION - %s\n" +
            "MESSAGE - %s\n" +
            "STACK_TRACE -\n%s";

    public void listen() {
        helper.bot().setUpdatesListener(updates -> {

            log.info("Updates size : [{}]", updates.size());

            for (Update update : updates) {
if(update.message() == null || update.callbackQuery() == null){
continue;
}
                RequestType request = RequestType.determineRequest(update);
System.out.println(update);
System.out.println(update.message());
                Integer userId = request.equals(RequestType.UPDATE) ?
                        update.message().from().id() :
                        update.callbackQuery().from().id();

                Long chatId = request.equals(RequestType.UPDATE) ?
                        update.message().chat().id() :
                        update.callbackQuery().message().chat().id();

                log.info("Working on Update ID : [{}], User ID : [{}], Chat ID : [{}], RequestType : [{}]", update.updateId(), userId, chatId, request.name());

                try {
                    handler.processRequest(update); // processing
                } catch (CommandNotFoundException e){ // UI exception
                    printLogException(userId, chatId, e);
                    alert(userId, e);

                    String customMessage = messageCreatorService.commandNotFoundCreator(update.message().text());
                    helper.executor().sendText(chatId, customMessage);
                    actionLogService.failedLog(String.valueOf(userId), TelegramCommand.UNDEFINED, e.getMessage());
                    return update.updateId();
                } catch (EntityNotFoundException e){ // UI exception
                    printLogException(userId, chatId, e);
                    alert(userId, e);

                    String customMessage = messageCreatorService.exceptionMessageCreator(NOT_SPECIAL);
                    if(e.getMessage() != null && e.getMessage().equals(PRAY_TIME_SETTLEMENT_NOT_FOUND)){
                        customMessage = messageCreatorService.exceptionMessageCreator(PRAY_TIME_SETTLEMENT_NOT_FOUND);
                        helper.executor().sendText(chatId, customMessage);

                        MessageDto registerNotCompleted = messageCreatorService.selectSourceCreator(0);
                        helper.executor().sendText(chatId, registerNotCompleted);
                    } else{
                        helper.executor().sendText(chatId, customMessage);
                    }

                    actionLogService.successLog(String.valueOf(chatId),TelegramCommand.UNDEFINED, customMessage);

                    return update.updateId();
                } catch (Exception e) { // technical exception

                    e.printStackTrace();

                    printLogException(userId, chatId, e);

                    // alert
                    alert(userId, e);

                    actionLogService.successLog(String.valueOf(chatId),TelegramCommand.UNDEFINED, e.getMessage());
                    return update.updateId();
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void alert(Integer userId, Exception e){
        helper.executor().sendText(
                ADMIN_TELEGRAM_ID,
                String.format(
                        ERROR_MESSAGE_TEMPLATE,
                        userId,
                        e.getClass().getSimpleName(),
                        e.getMessage(),
                        Arrays.toString(e.getStackTrace())
                )
        );
    }

    private void printLogException(Integer userId, Long chatId, Exception e){
        log.error("User ID [{}], Chat ID [{}], Exception [{}], Message [{}]", userId, chatId, e.getClass().getSimpleName(), e.getMessage());
    }

}
