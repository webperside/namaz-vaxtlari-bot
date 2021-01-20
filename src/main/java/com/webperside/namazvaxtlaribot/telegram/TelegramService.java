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
import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.enums.Emoji;
import com.webperside.namazvaxtlaribot.enums.telegram.TelegramCommand;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.UserRepository;
import com.webperside.namazvaxtlaribot.service.FileService;
import com.webperside.namazvaxtlaribot.service.SourceService;
import com.webperside.namazvaxtlaribot.util.MessageCreatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.webperside.namazvaxtlaribot.telegram.TelegramConfig.execute;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final SourceService sourceService;
    private final FileService fileService;
    private final MessageCreatorUtil messageCreatorUtil;

    public void process(Update update) throws IOException {
        System.out.println(update);
        if (update.message() == null && update.callbackQuery() != null) {
            processCallback(update);
        } else {
            String text = update.message().text();

            if ("test".equals(text)) {
                User user = userRepository.findAll().get(0);
                utilProcessSelectSource(Long.valueOf(user.getUserTgId()));
            } else if (TelegramCommand.START.getCommand().equals(text)) {
                processStart(update);
            } else {
                fileService.write(getUserInfo(update.message().from()) + " " + update.message().from().id() + " " + Instant.now().toString(),update.message().text() + " " + update.message().chat().id());
            }

        }
    }

//    public void sendDate(String key, DateDto dateDto) {
//        try {
//            String msg = null;
//            DateFormat df = new SimpleDateFormat("HH:mm dd.MM.yyyy");
//
//            Instant current = Instant.now().plus(Duration.ofHours(4));
//
//            Date dateSubh = df.parse(dateDto.getSubh() + " " + key);
//            Instant subh = dateSubh.toInstant();
//            Date dateZohr = df.parse(dateDto.getZohr() + " " + key);
//            Instant zohr = dateZohr.toInstant();
//            Date dateEsr = df.parse(dateDto.getEsr() + " " + key);
//            Instant esr = dateEsr.toInstant();
//            Date dateAxsham = df.parse(dateDto.getAxsham() + " " + key);
//            Instant axsham = dateAxsham.toInstant();
//            Date dateIsha = df.parse(dateDto.getIsha() + " " + key);
//            Instant isha = dateIsha.toInstant();
//
//            if (current.isBefore(subh)) {
//                long minutes = Duration.between(current, subh).toMinutes();
//                if (minutes == 0) {
//                    msg = "Sübh namazının vaxtıdır";
//                } else if (minutes <= 20 && minutes % 5 == 0) {
//                    msg = String.format("Sübh namazına %d dəqiqə qalıb", minutes);
//                }
//            } else if(current.isBefore(zohr)){
//                long minutes = Duration.between(current, zohr).toMinutes();
//                if (minutes == 0) {
//                    msg = "Zöhr namazının vaxtıdır";
//                } else if (minutes <= 20 && minutes % 5 == 0) {
//                    msg = String.format("Zöhr namazına %d dəqiqə qalıb", minutes);
//                }
//            } else if(current.isBefore(esr)){
//                long minutes = Duration.between(current, esr).toMinutes();
//                if (minutes == 0) {
//                    msg = "Əsr namazının vaxtıdır";
//                } else if (minutes <= 20 && minutes % 5 == 0) {
//                    msg = String.format("Əsr namazına %d dəqiqə qalıb", minutes);
//                }
//            } else if(current.isBefore(axsham)){
//                long minutes = Duration.between(current, axsham).toMinutes();
//                if (minutes == 0) {
//                    msg = "Axşam namazının vaxtıdır";
//                } else if (minutes <= 20 && minutes % 5 == 0) {
//                    msg = String.format("Axşam namazına %d dəqiqə qalıb", minutes);
//                }
//            } else if(current.isBefore(isha)){
//                long minutes = Duration.between(current, isha).toMinutes();
//                if (minutes == 0) {
//                    msg = "İşa namazının vaxtıdır";
//                } else if (minutes <= 20 && minutes % 5 == 0) {
//                    msg = String.format("İşa namazına %d dəqiqə qalıb", minutes);
//                }
//            }
//
//            if(msg != null){
//                sendBulkMessages(msg);
//            }
//        } catch (ParseException e) {
//            log.error(e.getMessage());
//        }
//
//    }

    private void processCallback(Update update){
        execute(new AnswerCallbackQuery(update.callbackQuery().id()));
        long userTgId = update.callbackQuery().message().chat().id();
        int msgId = update.callbackQuery().message().messageId();
        String callback = update.callbackQuery().data();

        if(callback.equals(Constants.BUTTON_CB_SELECT_SOURCE)) {
            utilProcessSelectSource(userTgId);
        } else if(callback.contains(Constants.BUTTON_CB_SELECT_SOURCE_NAVIGATE)){
            String[] params = callback.split(Constants.PARAM_SEPARATOR);
            String navigateTo = params[2];
            int page = Integer.parseInt(params[3]);
            utilProcessSelectSourceNavigate(userTgId, navigateTo, msgId, page);
        } else if(callback.contains(Constants.BUTTON_CB_SELECT_SOURCE_DESCRIPTION)){
            String[] params = callback.split(Constants.PARAM_SEPARATOR);
            Integer sourceId = Integer.valueOf(params[2]);
            int page = Integer.parseInt(params[3]);
            utilProcessSelectSourceDescription(userTgId, sourceId, msgId, page);
        } else if(callback.contains(Constants.BUTTON_CB_SELECT_CITY)){
            Integer sourceId = Integer.valueOf(callback.split(Constants.PARAM_SEPARATOR)[2]);
            utilProcessSelectCity(userTgId, sourceId, msgId);
        } else if(callback.contains(Constants.BUTTON_CB_SELECT_CITY_NAVIGATE)){
            String[] params = callback.split(Constants.PARAM_SEPARATOR);
            String navigateTo = params[2];
            Integer sourceId = Integer.parseInt(params[3]);
            int page = Integer.parseInt(params[4]);
            utilProcessSelectCityNavigate(userTgId, navigateTo, sourceId, msgId, page);
        }


//        } else if(callback.contains(Constants.BUTTON_CB_SELECT_SOURCE)){
//            String[] params = callback.split(Constants.PARAM_SEPARATOR);
//            Integer sourceId = Integer.valueOf(params[2]);
//            int page = Integer.parseInt(params[3]);
//            utilProcessSelectSourceDescription(userTgId, sourceId, msgId, page);
//        } else if(callback.contains(Constants.BUTTON_CB_SELECT_SOURCE_NAVIGATE)){
//            String[] params = callback.split(Constants.PARAM_SEPARATOR);
//            String navigateTo = params[2];
//            int page = Integer.parseInt(params[3]);
//            utilProcessSelectSourceNavigate(userTgId, navigateTo, msgId, page);
//        } else if(callback.contains(Constants.BUTTON_CB_SELECT_SOURCE_DESCRIPTION)){
//            int sourceId = Integer.parseInt(callback.split(Constants.PARAM_SEPARATOR)[2]);
//
//        } else if(callback.contains(Constants.BUTTON_CB_SELECT_SOURCE_DESCRIPTION_NAVIGATE)){
//            int page = Integer.parseInt(callback.split(Constants.PARAM_SEPARATOR)[2]);
//        }

    }

    private void processStart(Update update) {
        long chatId = update.message().chat().id();

        if (userRepository.existsByUserTgId(String.valueOf(chatId))) {
            String alreadyExist = messageSource.getMessage("telegram.user_already_exist", null, Locale.getDefault());
            sendMessage(chatId, alreadyExist);
        }

        userRepository.save(User.builder()
                .userTgId(String.valueOf(chatId)) // as user id
                .build());

        String from = getUserInfo(update.message().from());
        String startMessage = messageSource.getMessage("telegram.command.start", new Object[]{from}, Locale.getDefault());
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(Constants.BUTTON_T_BASHLA)
                        .callbackData(Constants.BUTTON_CB_SELECT_SOURCE)
        );
        sendMessageWithKeyboard(chatId, startMessage, markup);
    }

    private void utilProcessSelectSource(Long userTgId) {
        MessageDto dto = messageCreatorUtil.selectSourceCreator(0);
        sendMessageWithKeyboard(userTgId, dto.getMessage(), dto.getMarkup());
    }

    private void utilProcessSelectSourceDescription(Long userTgId, Integer sourceId, Integer messageId, int page){
        Source source = sourceService.findById(sourceId).orElseThrow(EntityNotFoundException::new);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(Emoji.LEFT_ARROW.getValue())
                        .callbackData(Constants.BUTTON_CB_SELECT_SOURCE_NAVIGATE + Emoji.LEFT_ARROW.getCallback() + Constants.PARAM_SEPARATOR + page),
                new InlineKeyboardButton(Constants.BUTTON_T_SELECT_SOURCE_CONFIRM)
                        .callbackData(Constants.BUTTON_CB_SELECT_CITY + sourceId)
        );

        String customMessage = source.getDescription();
        editMessageWithKeyboard(userTgId, customMessage, markup, messageId);
    }

    private void utilProcessSelectSourceNavigate(Long userTgId, String navigateTo, Integer messageId, int page){
        if(navigateTo.equals(Emoji.LEFT_ARROW.getCallback()) && page != 0){
            page--;
        } else {
            page++;
        }

        MessageDto dto = messageCreatorUtil.selectSourceCreator(page);
        editMessageWithKeyboard(userTgId, dto.getMessage(), dto.getMarkup(), messageId);
    }

    private void utilProcessSelectCity(Long userTgId, Integer sourceId, Integer messageId){
        MessageDto dto = messageCreatorUtil.selectCityCreator(0,sourceId);
        editMessageWithKeyboard(userTgId, dto.getMessage(), dto.getMarkup(), messageId);
    }

    private void utilProcessSelectCityNavigate(Long userTgId, String navigateTo, Integer sourceId, Integer messageId, Integer page){
        if(navigateTo.equals(Emoji.LEFT_ARROW.getCallback()) && page != 0){
            page--;
        } else {
            page++;
        }

        MessageDto dto = messageCreatorUtil.selectCityCreator(page, sourceId);
        editMessageWithKeyboard(userTgId, dto.getMessage(), dto.getMarkup(), messageId);
    }

    private void sendMessage(long chatId, String message) {
        ChatAction action = ChatAction.typing;

        execute(new SendChatAction(chatId, action));
        execute(new SendMessage(chatId, message));
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

    private String getUserInfo(com.pengrad.telegrambot.model.User user) {
        String firstName = user.firstName();
        String lastName = user.lastName();

        return firstName + (lastName != null ? " " + lastName : "");
    }
}
