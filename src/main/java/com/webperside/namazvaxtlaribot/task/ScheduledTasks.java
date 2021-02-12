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
    private static final long DAY = HOUR * 24;
    private static final String MONTH_CRON = "0 0 2 1 1/1 *";
    private static final long TEST = 5000;

    private final TaskService taskService;

    @Scheduled(cron = MONTH_CRON)
    public void jobGetPrayTimeAhlibeytAz(){// monthly
        taskService.getPrayTimesFromAhlibeytAz();
    }

    @Scheduled(fixedDelay = HOUR)
    public void jobStoreUserData(){taskService.storeUserData();}
}
