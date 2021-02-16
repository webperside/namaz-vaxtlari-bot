package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.telegram.enums.RequestType;
import com.webperside.namazvaxtlaribot.telegram.handlers.TelegramHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.webperside.namazvaxtlaribot.config.Constants.ADMIN_TELEGRAM_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramHelper {

    @Value("${TGT}")
    private String TOKEN;
    private TelegramBot bot;
    private final Executor executor = new Executor();

    public TelegramBot bot() {
        if (this.bot != null) {
            return bot;
        }
        this.bot = new TelegramBot(this.TOKEN);
        return this.bot;
    }

    public Executor executor() {
        return executor;
    }

    /**
     * @author Hamid Sultanzadeh
     * @implNote This class implemented for Telegram queries.For example, send, edit text messages or delete messages
     */
    @Service
    public class Executor {


        //        public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(T request){
//            return bot().execute(request);
//        }
//      todo just for now return nothing
        public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(T request) {
            bot().execute(request);
        }

        public void simulateTyping(Long userTgId) {
            ChatAction action = ChatAction.typing;
            execute(new SendChatAction(userTgId, action));
        }

        // start - core methods
        private void coreSendText(Long userTgId, String message, InlineKeyboardMarkup markup) {
            if (markup == null) {
                markup = new InlineKeyboardMarkup();
            }
            execute(new SendMessage(userTgId, message).replyMarkup(markup));
        }

        private void coreEditText(Long userId, Integer messageId, String message, InlineKeyboardMarkup markup) {
            if (markup == null) {
                markup = new InlineKeyboardMarkup();
            }
            execute(new EditMessageText(userId, messageId, message).replyMarkup(markup));
        }
        // finish - core methods

        // start - query methods
        public void sendText(Long userTgId, String message) {
            simulateTyping(userTgId);
            coreSendText(userTgId, message, null);
        }

        public void sendText(Long userTgId, MessageDto dto) {
            simulateTyping(userTgId);
            coreSendText(userTgId, dto.getMessage(), dto.getMarkup());
        }

        public void editText(Long userTgId, Integer messageId, String message) {
            coreEditText(userTgId, messageId, message, null);
        }

        public void editText(Long userTgId, Integer messageId, MessageDto dto) {
            coreEditText(userTgId, messageId, dto.getMessage(), dto.getMarkup());
        }

        public void deleteMessage(Long userTgId, Integer messageId) {
            execute(new DeleteMessage(userTgId, messageId));
        }

        public void answerCallbackQuery(String callbackQueryId){
            execute(new AnswerCallbackQuery(callbackQueryId));
        }

        public String getUserInfo(User user) {
            String firstName = user.firstName();
            String lastName = user.lastName();

            return firstName + (lastName != null ? " " + lastName : "");
        }
        // finish - query methods
    }

}
