package com.webperside.namazvaxtlaribot.task;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.external.APIService;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.service.TaskService;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final long HOUR = 360000;
    private static final long MINUTE = 60000;
    private static final long TEST = 5000;

//    private final APIService apiService;
//    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
//    private final TelegramService telegramService;

    private final TaskService taskService;

//    @Scheduled(initialDelay = TEST,fixedDelay = TEST)
//    public void sendPrayTimesTask(){
//        for(User user : Constants.users){
//            taskService.sendPrayTimes(user);
//        }
//    }

//    @Scheduled(fixedDelay = HOUR)
//    public void storeUserData(){
//        taskService.storeUserData();
//    }


//    @Scheduled(cron = "0 0 23 * * ?")
//    public void getDates() {
//        if (dates.size() == 0) {
//            apiService.get();
//        }
//    }
//
//    @Scheduled(cron = "0 50 23 * * ?")
//    public void removeDate() {
//        String key = SIMPLE_DATE_FORMAT.format(new Date());
//        dates.remove(key);
//    }
//
//
//    @Scheduled(initialDelay = 60000,fixedDelay = 60000)
//    public void sendDate() {
//        String key = SIMPLE_DATE_FORMAT.format(new Date());
//        telegramService.sendDate(key, dates.get(key));
//    }
}
