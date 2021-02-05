package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.service.CityService;
import com.webperside.namazvaxtlaribot.service.TaskService;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserService userService;
    private final CityService cityService;


    @Override
    public void sendPrayTimes(User user) {
//        CitySettlement citySettlement = user.getCitySettlement();

//        if(!prayTimes.containsKey(citySettlement.getId())){ // if data does not exist

//        }


    }

    @Override
    public void storeUserData() {
        Constants.users = userService.getAll();
    }

    // private util methods

    private void webscrapData(Integer citySettlementId){

    }
}
