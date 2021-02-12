package com.webperside.namazvaxtlaribot.service.impl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.PrayTimeDto;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.webperside.namazvaxtlaribot.config.Constants.DS_AHLIBEYT_AZ;
import static com.webperside.namazvaxtlaribot.config.Constants.ahlibeytAzTimes;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserService userService;
    private final CityService cityService;
    private final SourceService sourceService;
    private final WebscrapService webscrapService;


    @Override
    public void sendPrayTimes(User user) {

    }

    @Override
    public void storeUserData() {
        Constants.users = userService.getAll();
    }

    @Override
    public void getPrayTimesFromAhlibeytAz() {
        Source source = sourceService.findByName(DS_AHLIBEYT_AZ);
        webscrapService.prepareDataForAhlibeytAz(source);
    }
}
