package com.webperside.namazvaxtlaribot.task;

import com.webperside.namazvaxtlaribot.external.APIService;
import com.webperside.namazvaxtlaribot.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.webperside.namazvaxtlaribot.config.Config.dates;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final APIService apiService;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private final TelegramService telegramService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void getDates() {
        if (dates.size() == 0) {
            apiService.get();
        }
    }

    @Scheduled(cron = "0 50 23 * * ?")
    public void removeDate() {
        String key = SIMPLE_DATE_FORMAT.format(new Date());
        dates.remove(key);
    }


    @Scheduled(initialDelay = 60000,fixedDelay = 60000)
    public void sendDate() {
        String key = SIMPLE_DATE_FORMAT.format(new Date());
        telegramService.sendDate(key, dates.get(key));
    }
}
