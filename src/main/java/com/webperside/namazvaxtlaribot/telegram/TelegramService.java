package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@Slf4j
public class TelegramService {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public void process(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

//        if (TelegramCommand.START.getCommand().equals(text)) {
//            if (writeToFile(update.message().chat().id())) {
//                sendMessage(chatId, formatMessage(TelegramMessage.START_MESSAGE, getUserInfo(update.message().from())));
//            }
//        }
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

    private void sendMessage(long chatId, String message) {
        TelegramConfig.getInstance().execute(new SendMessage(chatId, message));
    }

    private String formatMessage(TelegramMessage telegramMessage, String message) {
        return String.format(telegramMessage.getMessage(), message);
    }

    private String getUserInfo(User user) {
        String firstName = user.firstName();
        String lastName = user.lastName();

        return firstName + (lastName != null ? " " + lastName : "");
    }
}
