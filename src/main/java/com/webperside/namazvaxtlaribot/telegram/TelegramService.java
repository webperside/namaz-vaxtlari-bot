package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.enums.Emoji;
import com.webperside.namazvaxtlaribot.enums.telegram.TelegramCommand;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.UserRepository;
import com.webperside.namazvaxtlaribot.service.SourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    public void process(Update update) {
        // as user id
        System.out.println(update);
        if (update.message() == null && update.callbackQuery() != null) {
            execute(new AnswerCallbackQuery(update.callbackQuery().id()));
            System.out.println(update.callbackQuery().data());
//            sendMessage(update.callbackQuery().message().chat().id(), update.callbackQuery().message().text());
        } else {
            String text = update.message().text();

            if ("test".equals(text)) {
                User user = userRepository.findAll().get(0);
                utilProcessSelectSource(user);
            } else if (TelegramCommand.START.getCommand().equals(text)) {
                User user = processStart(update);
                utilProcessSelectSource(user);
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

    private User processStart(Update update) {
        long chatId = update.message().chat().id();

        if (userRepository.existsByUserTgId(String.valueOf(chatId))) {
            String alreadyExist = messageSource.getMessage("telegram.user_already_exist", null, Locale.getDefault());
            sendMessage(chatId, alreadyExist);
            return null;
        }

        User user = userRepository.save(User.builder()
                .userTgId(String.valueOf(chatId)) // as user id
                .build());

        String from = getUserInfo(update.message().from());
        String startMessage = messageSource.getMessage("telegram.command.start", new Object[]{from}, Locale.getDefault());
        sendMessage(update.message().chat().id(), startMessage);

        return user;
    }

    private void utilProcessSelectSource(User user) {
        Page<Source> sources = sourceService.getAll(0);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        sources.getContent().forEach(source -> {
            markup.addRow(Collections.singletonList(new InlineKeyboardButton(
                    source.getName()).callbackData(Constants.CALLBACK_VAL + source.getName())).toArray(new InlineKeyboardButton[0]));
        });

        List<InlineKeyboardButton> navigator = new ArrayList<>();

        if (sources.hasNext()) {
            navigator.add(new InlineKeyboardButton(
                    Emoji.RIGHT_ARROW.getValue()).callbackData(Constants.CALLBACK_VAL + Emoji.RIGHT_ARROW.getCallback()));
        }

        if (sources.hasPrevious()) {
            navigator.add(new InlineKeyboardButton(
                    Emoji.LEFT_ARROW.getValue()).callbackData(Constants.CALLBACK_VAL + Emoji.LEFT_ARROW.getCallback()));
        }

        markup.addRow(navigator.toArray(new InlineKeyboardButton[0]));

        long userTgId = Long.parseLong(user.getUserTgId());
        String sourceSelect = messageSource.getMessage("telegram.select_source", null, Locale.getDefault());
        sendMessageWithKeyboard(userTgId, sourceSelect, markup);
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

    private String getUserInfo(com.pengrad.telegrambot.model.User user) {
        String firstName = user.firstName();
        String lastName = user.lastName();

        return firstName + (lastName != null ? " " + lastName : "");
    }
}
