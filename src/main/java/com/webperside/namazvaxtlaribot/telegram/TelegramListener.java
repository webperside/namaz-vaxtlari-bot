package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.ChatMemberUpdated;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.service.ActionLogService;
import com.webperside.namazvaxtlaribot.service.MessageCreatorService;
import com.webperside.namazvaxtlaribot.task.ScheduledTasks;
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

    private static final String ERROR_MESSAGE_TEMPLATE =
            "CHAT/USER ID - %s\n" +
            "EXCEPTION - %s\n" +
            "MESSAGE - %s\n" +
            "STACK_TRACE -\n%s";

    public void listen() {
//        helper.bot().setUpdatesListener(updates -> {
//
//            log.info("Updates size : [{}]", updates.size());
//
//            try{
//                for (Update update : updates) {
//                    if(!checkForwardIsOk(update)) continue;
//
//                    RequestType request = RequestType.determineRequest(update);
//
//                    Long chatId = request.equals(RequestType.UPDATE) ?
//                            update.message().chat().id() :
//                            update.callbackQuery().message().chat().id();
//
//                    log.info("Working on Update ID : [{}], Chat/User ID : [{}], RequestType : [{}]", update.updateId(), chatId, request.name());
//
//                    try {
//                        handler.processRequest(update); // processing
//                    } catch (CommandNotFoundException e){ // UI exception
//                        return catchCommandNotFoundException(update, chatId, e); // return updateId
//                    } catch (EntityNotFoundException e){ // UI exception
//                        return catchEntityNotFoundException(update, chatId, e); // return updateId
//                    } catch (Exception e) { // technical exception
//                        return catchException(update, chatId, e); // return updateId
//                    }
//                }
//                return UpdatesListener.CONFIRMED_UPDATES_ALL;
//            } catch (Exception iDontKnow){
//                iDontKnow.printStackTrace();
//                sleep();
//                return UpdatesListener.CONFIRMED_UPDATES_NONE;
//            }
//        });
    }

    public void filterRequest(Update update){
        try{
            if(!checkForwardIsOk(update)) return;

            RequestType request = RequestType.determineRequest(update);

            Long chatId = request.equals(RequestType.UPDATE) ?
                    update.message().chat().id() :
                    update.callbackQuery().message().chat().id();

            log.info("Working on Update ID : [{}], Chat/User ID : [{}], RequestType : [{}]", update.updateId(), chatId, request.name());

            try {
                handler.processRequest(update); // processing
            } catch (CommandNotFoundException e){ // UI exception
                catchCommandNotFoundException(update, chatId, e); // return updateId
            } catch (EntityNotFoundException e){ // UI exception
                catchEntityNotFoundException(update, chatId, e); // return updateId
            } catch (Exception e) { // technical exception
                catchException(update, chatId, e); // return updateId
            }
        }catch(Exception iDontKnow){
            iDontKnow.printStackTrace();
        }
    }

    private void sleep(){
        try {
            Thread.sleep(ScheduledTasks.MINUTE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkForwardIsOk(Update update){
        if(update.message() == null && update.callbackQuery() == null){
            helper.executor().sendText(
                    ADMIN_TELEGRAM_ID,
                    update.toString()
            );

            ChatMemberUpdated cmu = update.myChatMember();
            String id;

            if(cmu != null && cmu.from() != null) id = cmu.from().id() + "";
            else id = ADMIN_TELEGRAM_ID + "";

            actionLogService.failedLog(
                    id,
                    TelegramCommand.UNDEFINED,
                    "The request did not come from Telegram"
            );

            return false;
        }
        return true;
    }

    private Integer catchCommandNotFoundException(Update update, Long chatId, CommandNotFoundException e){
        printLogException(chatId, e);
        alert(chatId, e);

        String customMessage = messageCreatorService.commandNotFoundCreator(update.message().text());
        helper.executor().sendText(chatId, customMessage);
        actionLogService.failedLog(String.valueOf(chatId), TelegramCommand.UNDEFINED, e.getMessage());
        return update.updateId();
    }

    private Integer catchEntityNotFoundException(Update update, Long chatId, EntityNotFoundException e){
        printLogException(chatId, e);
        alert(chatId, e);

        String customMessage = messageCreatorService.exceptionMessageCreator(NOT_SPECIAL);
        if(e.getMessage() != null && e.getMessage().equals(PRAY_TIME_SETTLEMENT_NOT_FOUND)){
            customMessage = messageCreatorService.exceptionMessageCreator(PRAY_TIME_SETTLEMENT_NOT_FOUND);
            helper.executor().sendText(chatId, customMessage);

            MessageDto registerNotCompleted = messageCreatorService.selectSourceCreator(0);
            helper.executor().sendText(chatId, registerNotCompleted);
        } else{
            helper.executor().sendText(chatId, customMessage);
        }

        actionLogService.failedLog(String.valueOf(chatId),TelegramCommand.UNDEFINED, customMessage);

        return update.updateId();

    }

    private Integer catchException(Update update, Long chatId, Exception e){
        e.printStackTrace();

        printLogException(chatId, e);

        // alert
        alert(chatId, e);

        actionLogService.failedLog(String.valueOf(chatId),TelegramCommand.UNDEFINED, e.getMessage());
        return update.updateId();
    }

    private void alert(Long chatId, Exception e){
        helper.executor().sendText(
                ADMIN_TELEGRAM_ID,
                String.format(
                        ERROR_MESSAGE_TEMPLATE,
                        chatId,
                        e.getClass().getSimpleName(),
                        e.getMessage(),
                        Arrays.toString(e.getStackTrace())
                )
        );
    }

    private void printLogException(Long chatId, Exception e){
        log.error("Chat/User ID [{}], Exception [{}], Message [{}]", chatId, e.getClass().getSimpleName(), e.getMessage());
    }

}
