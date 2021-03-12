package com.webperside.namazvaxtlaribot.task;

import com.webperside.namazvaxtlaribot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    public static final long MINUTE = 60000;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;
    private static final String MONTH_CRON = "0 0 2 1 1/1 *";
    private static final String DAY_CRON = "0 0 0 * * *";
    private static final long TEST = 5000;

    private final TaskService taskService;

    @Scheduled(cron = MONTH_CRON)
    public void jobGetPrayTimeAhlibeytAz(){// monthly
        taskService.getPrayTimesFromAhlibeytAz();
    }

//    @Scheduled(fixedDelay = HOUR)
//    public void jobStoreUserData(){taskService.storeUserData();}

    @Scheduled(cron = DAY_CRON)
    public void jobClearPrayTimeData(){
        taskService.clearPrayTimeData();
    }
}
