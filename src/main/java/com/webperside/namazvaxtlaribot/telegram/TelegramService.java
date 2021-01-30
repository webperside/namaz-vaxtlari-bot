package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.util.Params;
import com.webperside.namazvaxtlaribot.enums.Emoji;
import com.webperside.namazvaxtlaribot.enums.telegram.TelegramCommand;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.service.FileService;
import com.webperside.namazvaxtlaribot.service.SourceService;
import com.webperside.namazvaxtlaribot.service.UserService;
import com.webperside.namazvaxtlaribot.util.CommonUtil;
import com.webperside.namazvaxtlaribot.util.MessageCreatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;

import static com.webperside.namazvaxtlaribot.config.Constants.*;
import static com.webperside.namazvaxtlaribot.telegram.TelegramConfig.execute;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private final MessageSource messageSource;
    private final UserService userService;
    private final SourceService sourceService;
    private final FileService fileService;
    private final MessageCreatorUtil messageCreatorUtil;

    // ...::: Telegram Updates handlers methods :::...

    public void process(Update update) throws IOException {
        if (update.message() == null && update.callbackQuery() != null) {
            processCallback(update);
        } else {
            String text = update.message().text();

            if (TelegramCommand.TEST.getCommand().equals(text)) {
                processTest(update);
            } else if (TelegramCommand.START.getCommand().equals(text)) {
                processStart(update);
            } else {
                fileService.write(getUserInfo(update.message().from()) + " " + update.message().from().id() + " " + Instant.now().toString(),update.message().text() + " " + update.message().chat().id());
            }

        }
    }

    private void processCallback(Update update){
        CallbackQuery query = update.callbackQuery();
        long userTgId = query.message().chat().id();
        int msgId = query.message().messageId();
        String callback = query.data();
        Params params = Params.split(callback);
        String main = params.getMain();
        Map<String, String> values = params.getValues();

        switch (main) {
            case BUTTON_CB_SELECT_SOURCE: {
                String navigateTo = values.get(NAVIGATE_TO);
                Integer sourcePage = Integer.parseInt(values.get(SOURCE_PAGE));
                if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                    utilProcessSelectSource(userTgId);
                } else {
                    utilProcessSelectSourceNavigate(userTgId, navigateTo, msgId, sourcePage);
                }
                break;
            }
            case BUTTON_CB_SELECT_SOURCE_DESCRIPTION: {
                Integer sourceId = Integer.valueOf(values.get(SOURCE_ID));
                Integer sourcePage = Integer.parseInt(values.get(SOURCE_PAGE));
                utilProcessSelectSourceDescription(userTgId, sourceId, msgId, sourcePage);
                break;
            }
            case BUTTON_CB_SELECT_CITY: {
                String navigateTo = values.get(NAVIGATE_TO);
                Integer cityPage = Integer.parseInt(values.get(CITY_PAGE));
                Integer sourceId = Integer.parseInt(values.get(SOURCE_ID));
                Integer sourcePage = Integer.parseInt(values.get(SOURCE_PAGE));

                if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                    utilProcessSelectCity(userTgId, sourceId, sourcePage, msgId);
                } else {
                    utilProcessSelectCityNavigate(userTgId, navigateTo, cityPage, sourceId, sourcePage, msgId);
                }
                break;
            }
            case BUTTON_CB_SELECT_CITY_DESCRIPTION: {
                Integer cityId = Integer.parseInt(values.get(CITY_ID));
                Integer cityPage = Integer.parseInt(values.get(CITY_PAGE));
                Integer sourceId = Integer.parseInt(values.get(SOURCE_ID));
                Integer sourcePage = Integer.parseInt(values.get(SOURCE_PAGE));
                utilProcessSelectCityDescription(userTgId, cityId, cityPage, sourceId, sourcePage, msgId);
                break;
            }
            case BUTTON_CB_SELECT_CITY_SETT_DESCRIPTION: { // if city has settlements

                break;
            }
            case BUTTON_CB_SELECT_CITY_SETT_CONFIRM: { // final endpoint
                Integer citySettlementId = Integer.parseInt(values.get(CITY_SETT_ID));
                String msg = messageCreatorUtil.selectCitySettlementConfirmCreator(
                        getUserInfo(query.from()),
                        userTgId,
                        citySettlementId
                );
                sendMessage(userTgId, msg);
                break;
            }

        }

        execute(new AnswerCallbackQuery(update.callbackQuery().id()));
    }

    private void processTest(Update update){
        long chatId = update.message().chat().id();
        MessageDto dto = messageCreatorUtil.testCreator();
        sendMessageWithKeyboard(chatId, dto);
    }

    private void processStart(Update update) {
        long chatId = update.message().chat().id();

        if (userService.existsByTgId(String.valueOf(chatId))) {
            String alreadyExist = messageSource.getMessage("telegram.user_already_exist", null, Locale.getDefault());
            sendMessage(chatId, alreadyExist);
            return ;
        }

        userService.save(String.valueOf(chatId));

        String from = getUserInfo(update.message().from());
        MessageDto dto = messageCreatorUtil.startCreator(from);
        sendMessageWithKeyboard(chatId, dto);
    }

    private void utilProcessSelectSource(Long userTgId) {
        MessageDto dto = messageCreatorUtil.selectSourceCreator(0);
        sendMessageWithKeyboard(userTgId, dto);
    }

    private void utilProcessSelectSourceDescription(Long userTgId, Integer sourceId, Integer messageId, int sourcePage){
        MessageDto dto = messageCreatorUtil.selectSourceDescriptionCreator(sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectSourceNavigate(Long userTgId, String navigateTo, Integer messageId, int page) {
        page = CommonUtil.getPageByValue(navigateTo, page);

        MessageDto dto = messageCreatorUtil.selectSourceCreator(page);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCity(Long userTgId, Integer sourceId, Integer sourcePage, Integer messageId){
        MessageDto dto = messageCreatorUtil.selectCityCreator(0,sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCityDescription(Long userTgId,Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage, Integer messageId){
        MessageDto dto = messageCreatorUtil.selectCityDescriptionCreator(cityId, cityPage, sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCityNavigate(Long userTgId, String navigateTo, Integer cityPage, Integer sourceId, Integer sourcePage, Integer messageId){
        cityPage = CommonUtil.getPageByValue(navigateTo, cityPage);

        MessageDto dto = messageCreatorUtil.selectCityCreator(cityPage, sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    // ...::: Telegram Updates handlers methods :::...

    // ...::: Telegram Message send and edit methods :::...

    private void sendMessage(long chatId, String message) {
        ChatAction action = ChatAction.typing;

        execute(new SendChatAction(chatId, action));
        execute(new SendMessage(chatId, message));
    }

    private void sendMessageWithKeyboard(long chatId, MessageDto dto){
        ChatAction action = ChatAction.typing;
        SendMessage sendMessage = new SendMessage(chatId, dto.getMessage()).replyMarkup(dto.getMarkup());

        execute(new SendChatAction(chatId, action));
        execute(sendMessage);
    }

    private void sendMessageWithKeyboard(long chatId, String message, InlineKeyboardMarkup markup) {
        ChatAction action = ChatAction.typing;
        SendMessage sendMessage = new SendMessage(chatId, message).replyMarkup(markup);

        execute(new SendChatAction(chatId, action));
        execute(sendMessage);
    }

    private void editMessageWithKeyboard(long chatId, String message, InlineKeyboardMarkup markup, int messageId){
        execute(new EditMessageText(chatId, messageId, message).replyMarkup(markup));
    }

    private void editMessageWithKeyboard(long chatId, MessageDto dto, int messageId){
        execute(new EditMessageText(chatId, messageId, dto.getMessage()).replyMarkup(dto.getMarkup()));
    }

    // ...::: Telegram Message send and edit methods :::...

    // ...::: Telegram User Info methods :::...

    private String getUserInfo(com.pengrad.telegrambot.model.User user) {
        String firstName = user.firstName();
        String lastName = user.lastName();

        return firstName + (lastName != null ? " " + lastName : "");
    }

    // ...::: Telegram User Info methods :::...
}
