package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.service.*;
import com.webperside.namazvaxtlaribot.telegram.TelegramHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.webperside.namazvaxtlaribot.config.Constants.ADMIN_TELEGRAM_ID;
import static com.webperside.namazvaxtlaribot.config.Constants.DS_AHLIBEYT_AZ;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TelegramHelper.Executor executor;
    private final SourceService sourceService;
    private final WebscrapService webscrapService;


    @Override
    public void sendPrayTimes(User user) {
    }

    @Override
    public void storeUserData() {
//        Constants.users = userService.getAll();
    }

    @Override
    public void getPrayTimesFromAhlibeytAz() {
//        executor.sendText(ADMIN_TELEGRAM_ID, "job : getPrayTimesFromAhlibeytAz");
        Source source = sourceService.findByName(DS_AHLIBEYT_AZ);
        webscrapService.prepareDataForAhlibeytAz(source);
    }

    @Override
    public void clearPrayTimeData() {
//        executor.sendText(ADMIN_TELEGRAM_ID, "job : clearPrayTimeData");
        Constants.prayTimes = new HashMap<>();
    }
}
