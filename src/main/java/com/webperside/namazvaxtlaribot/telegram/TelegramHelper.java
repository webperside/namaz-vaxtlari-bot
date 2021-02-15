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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.webperside.namazvaxtlaribot.config.Constants.ADMIN_TELEGRAM_ID;

@Service
@Slf4j
public class TelegramHelper {

    @Value("${TGT}")
    private String TOKEN;
    private TelegramBot bot;
    private final Executor executor = new Executor();
    private final Listener listener = new Listener();

    public TelegramBot bot() {
        if (this.bot != null) {
            return bot;
        }
        this.bot = new TelegramBot(this.TOKEN);
        return this.bot;
    }

    public void listen() {
        listener.listen();
    }

    public Executor executor() {
        return executor;
    }

    /**
     * @author Hamid Sultanzadeh
     * @implNote This class implemented for Telegram queries.For example, send, edit text messages or delete messages
     */
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

        public String getUserInfo(User user) {
            String firstName = user.firstName();
            String lastName = user.lastName();

            return firstName + (lastName != null ? " " + lastName : "");
        }
        // finish - query methods
    }

    /**
     * @author Hamid Sultanzadeh
     * @implNote This class implemented to listen Telegram Forwards
     */
    public class Listener {

        private static final String ERROR_MESSAGE_TEMPLATE = "USER ID - %s\n" +
                "EXCEPTION - %s\n" +
                "MESSAGE - %s";

        public void listen() {
            bot().setUpdatesListener(updates -> {

                log.info("Updates size : [{}]", updates.size());

                for (Update update : updates) {
                    RequestType request = RequestType.determineRequest(update);

                    Integer userId = request.equals(RequestType.UPDATE) ?
                            update.message().from().id() :
                            update.callbackQuery().from().id();

                    log.info("Working on Update ID : [{}], User ID : [{}], RequestType : [{}]", update.updateId(), userId, request.name());

                    try {
//                        telegramService.process(update);
                    } catch (Exception e) {

                        log.error("User ID [{}], Exception [{}], Message [{}]", userId, e.getClass().getSimpleName(), e.getMessage());

                        executor().sendText(
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

}
