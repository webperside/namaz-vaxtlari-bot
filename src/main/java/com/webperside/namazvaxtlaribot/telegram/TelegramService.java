package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.enums.telegram.TelegramCommand;
import com.webperside.namazvaxtlaribot.service.FileService;
import com.webperside.namazvaxtlaribot.service.MessageCreatorService;
import com.webperside.namazvaxtlaribot.service.UserService;
import com.webperside.namazvaxtlaribot.util.CommonUtil;
import com.webperside.namazvaxtlaribot.util.Params;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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
    private final FileService fileService;
    private final MessageCreatorService messageCreatorService;

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

        Params params = Params.split(query.data());
        String main = params.getMain();
        Map<String, String> values = params.getValues();

        // params
        String navigateTo = values.getOrDefault(NAVIGATE_TO, null);
        Integer sourcePage = Integer.parseInt(values.getOrDefault(SOURCE_PAGE, "0"));
        Integer sourceId = Integer.valueOf(values.getOrDefault(SOURCE_ID,"0"));
        Integer cityPage = Integer.parseInt(values.getOrDefault(CITY_PAGE,"0"));
        Integer cityId = Integer.parseInt(values.getOrDefault(CITY_ID,"0"));
        Integer settlementId = Integer.parseInt(values.getOrDefault(SETT_ID,"0"));


        switch (main) {
            case BUTTON_CB_SELECT_SOURCE: {
                if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                    utilProcessSelectSource(userTgId, msgId);
                } else {
                    utilProcessSelectSourceNavigate(userTgId, navigateTo, msgId, sourcePage);
                }
                break;
            }
            case BUTTON_CB_SELECT_SOURCE_DESCRIPTION: {
                utilProcessSelectSourceDescription(userTgId, sourceId, msgId, sourcePage);
                break;
            }
            case BUTTON_CB_SELECT_CITY: {
                if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                    utilProcessSelectCity(userTgId, sourceId, sourcePage, msgId);
                } else {
                    utilProcessSelectCityNavigate(userTgId, navigateTo, cityPage, sourceId, sourcePage, msgId);
                }
                break;
            }
            case BUTTON_CB_SELECT_CITY_DESCRIPTION: {
                utilProcessSelectCityDescription(userTgId, cityId, cityPage, sourceId, sourcePage, msgId);
                break;
            }
            case BUTTON_CB_SELECT_CITY_SETT_DESCRIPTION: { // if city has settlements
                utilProcessSelectCitySettDescription(userTgId, settlementId, cityId, cityPage, sourceId, sourcePage, msgId);
                break;
            }
            case BUTTON_CB_SELECT_CITY_SETT_CONFIRM: { // final endpoint
                utilProcessSelectCitySettConfirm(userTgId, settlementId, query.from(), msgId);
                break;
            }

        }

        execute(new AnswerCallbackQuery(update.callbackQuery().id()));
    }

    private void processTest(Update update){
        long chatId = update.message().chat().id();
        MessageDto dto = messageCreatorService.testCreator();
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
        MessageDto dto = messageCreatorService.startCreator(from);
        sendMessageWithKeyboard(chatId, dto);
    }

    private void utilProcessSelectSource(Long userTgId, Integer msgId) {
        MessageDto dto = messageCreatorService.selectSourceCreator(0);
        editMessageWithKeyboard(userTgId, dto, msgId);
//        sendMessageWithKeyboard(userTgId, dto);
    }

    private void utilProcessSelectSourceDescription(Long userTgId, Integer sourceId, Integer messageId, int sourcePage){
        MessageDto dto = messageCreatorService.selectSourceDescriptionCreator(sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectSourceNavigate(Long userTgId, String navigateTo, Integer messageId, int page) {
        page = CommonUtil.getPageByValue(navigateTo, page);

        MessageDto dto = messageCreatorService.selectSourceCreator(page);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCity(Long userTgId, Integer sourceId, Integer sourcePage, Integer messageId){
        MessageDto dto = messageCreatorService.selectCityCreator(0,sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCityDescription(Long userTgId, Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage, Integer messageId){
        MessageDto dto = messageCreatorService.selectCityDescriptionCreator(cityId, cityPage, sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCityNavigate(Long userTgId, String navigateTo, Integer cityPage, Integer sourceId, Integer sourcePage, Integer messageId){
        cityPage = CommonUtil.getPageByValue(navigateTo, cityPage);

        MessageDto dto = messageCreatorService.selectCityCreator(cityPage, sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCitySettDescription(Long userTgId, Integer citySettlementId, Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage, Integer messageId){
        MessageDto dto = messageCreatorService.selectCitySettlementDescriptionCreator(citySettlementId, cityId, cityPage, sourceId, sourcePage);
        editMessageWithKeyboard(userTgId, dto, messageId);
    }

    private void utilProcessSelectCitySettConfirm(Long userTgId, Integer citySettlementId, User user, Integer msgId){
        deleteMessage(userTgId, msgId);

        String msg = messageCreatorService.selectCitySettlementConfirmCreator(
                getUserInfo(user),
                userTgId,
                citySettlementId
        );
        sendMessage(userTgId, msg);
    }
    // ...::: Telegram Updates handlers methods :::...

    // ...::: Telegram Message send, edit, delete methods :::...

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

    private void deleteMessage(long chatId, int messageId){
        execute(new DeleteMessage(chatId, messageId));
    }

    // ...::: Telegram Message send and edit methods :::...

    // ...::: Telegram User Info methods :::...

    private String getUserInfo(User user) {
        String firstName = user.firstName();
        String lastName = user.lastName();

        return firstName + (lastName != null ? " " + lastName : "");
    }

    // ...::: Telegram User Info methods :::...
}
